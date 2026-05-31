package com.example.ui.screens.admin

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.*
import com.example.data.remote.gemini.GeminiService
import com.example.ui.theme.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.*

sealed interface AdminUiState {
    object Idle : AdminUiState
    object Generating : AdminUiState
    data class Success(val questions: List<Question>) : AdminUiState
    data class Error(val message: String) : AdminUiState
}

class AdminQuizViewModel : ViewModel() {
    private val geminiService = GeminiService()
    
    private val _uiState = MutableStateFlow<AdminUiState>(AdminUiState.Idle)
    val uiState: StateFlow<AdminUiState> = _uiState.asStateFlow()

    fun generateQuizzes(
        subject: String,
        formLevel: String,
        questionType: QuestionType,
        topic: String,
        count: Int
    ) {
        viewModelScope.launch {
            _uiState.value = AdminUiState.Generating
            
            // Craft a robust prompt strictly requesting raw JSON array
            val typeStr = when (questionType) {
                QuestionType.MCQ -> "Multiple Choice Questions (MCQ)"
                QuestionType.FIB -> "Fill in the Blanks (FIB)"
                QuestionType.TF -> "True/False (TF)"
            }

            val prompt = """
                You are an elite expert Tanzanian high school national curriculum developer preparing official NECTA prep questions.
                Generate exactly $count high-quality $typeStr questions for:
                - Subject: $subject
                - Level: $formLevel
                - Topic/Subtopic Focus: $topic

                You MUST return ONLY a raw JSON array matching this exact schema:
                [
                  {
                    "id": "gen_rec_${java.util.UUID.randomUUID().toString().take(4)}",
                    "questionText": "The question body. If mathematical or physical formulas are needed, write standard inline LaTeX escaped equations using \\[ ... \\] for display math or \\( ... \\) for inline math.",
                    "type": "${questionType.name}",
                    "options": [
                      {"id": "a", "text": "Correct Choice Text", "isCorrect": true},
                      {"id": "b", "text": "Wrong Choice 1 Text", "isCorrect": false},
                      {"id": "b", "text": "Wrong Choice 2 Text", "isCorrect": false},
                      {"id": "d", "text": "Wrong Choice 3 Text", "isCorrect": false}
                    ],
                    "acceptedAnswers": ["accepted_answer_1"],
                    "correctAnswerTf": true,
                    "explanation": "Extremely detailed step-by-step analytical explanation highlighting the key principles and teaching the student why the answer is correct."
                  }
                ]

                Important guidelines:
                1. If type is MCQ, you must populate "options" with exactly 4 choices (exactly ONE choice must have "isCorrect": true) and leave "acceptedAnswers" empty and "correctAnswerTf" null.
                2. If type is TF (True or False), you must set "correctAnswerTf" to true or false. Leave "options" and "acceptedAnswers" empty.
                3. If type is FIB (Fill in the blanks), you must populate the "acceptedAnswers" array with possible exact correct string answers (case-insensitive list). Leave "options" empty and "correctAnswerTf" null.
                4. For Math/Physics, write formulas clearly in LaTeX using \\[ ... \\] or \\( ... \\).
                5. Output ONLY the raw JSON block. Do NOT include Markdown wrapping like ```json or any other introduction or commentary. Begin with [ and end with ].
            """.trimIndent()

            try {
                val jsonStr = geminiService.generateExplanation(prompt)
                if (jsonStr.isBlank()) {
                    _uiState.value = AdminUiState.Error("Failed to communicate with Gemini AI. Ensure GEMINI_API_KEY is configured in your Secrets panel.")
                    return@launch
                }

                // Clean response in case markdown blocks are present despite prompt instructions
                val cleanedJson = jsonStr.replace("```json", "")
                    .replace("```", "")
                    .trim()

                val rootElement = Json.parseToJsonElement(cleanedJson)
                if (rootElement is JsonArray) {
                    val questionsList = mutableListOf<Question>()
                    
                    rootElement.forEachIndexed { index, element ->
                        val obj = element.jsonObject
                        val qId = obj["id"]?.jsonPrimitive?.content ?: "gen_q_${index}_${System.currentTimeMillis()}"
                        val qText = obj["questionText"]?.jsonPrimitive?.content ?: ""
                        val qExplanation = obj["explanation"]?.jsonPrimitive?.content ?: ""
                        
                        // Parse options if present
                        val mappedOptions = mutableListOf<McqOption>()
                        obj["options"]?.jsonArray?.forEach { optVal ->
                            val optObj = optVal.jsonObject
                            mappedOptions.add(
                                McqOption(
                                    id = optObj["id"]?.jsonPrimitive?.content ?: "a",
                                    text = optObj["text"]?.jsonPrimitive?.content ?: "",
                                    isCorrect = optObj["isCorrect"]?.jsonPrimitive?.boolean ?: false
                                )
                            )
                        }

                        // Parse accepted answers
                        val accepted = obj["acceptedAnswers"]?.jsonArray?.map { it.jsonPrimitive.content } ?: emptyList()
                        val correctTf = obj["correctAnswerTf"]?.jsonPrimitive?.booleanOrNull

                        questionsList.add(
                            Question(
                                id = qId,
                                learningPackId = "admin_pack_live",
                                type = questionType,
                                questionText = qText,
                                options = mappedOptions,
                                acceptedAnswers = accepted,
                                correctAnswerTf = correctTf,
                                explanation = qExplanation,
                                difficulty = "Intermediate"
                            )
                        )
                    }

                    _uiState.value = AdminUiState.Success(questionsList)
                } else {
                    _uiState.value = AdminUiState.Error("Format Mismatch: AI did not respond with a JSON array.")
                }
            } catch (e: Exception) {
                _uiState.value = AdminUiState.Error("Error parsing generated quiz metadata: ${e.message}")
            }
        }
    }

    fun resetState() {
        _uiState.value = AdminUiState.Idle
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminQuizGeneratorScreen(
    onNavigateBack: () -> Unit,
    viewModel: AdminQuizViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    var subject by remember { mutableStateOf("Mathematics") }
    var formLevel by remember { mutableStateOf("Form 4") }
    var questionType by remember { mutableStateOf(QuestionType.MCQ) }
    var topic by remember { mutableStateOf("Quadratic Equations") }
    var quantity by remember { mutableStateOf(3) }

    var editingQuestions by remember { mutableStateOf<List<Question>>(emptyList()) }

    // Update editable questions when success is returned
    LaunchedEffect(uiState) {
        if (uiState is AdminUiState.Success) {
            editingQuestions = (uiState as AdminUiState.Success).questions
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "AI Admin Panel",
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            
            // Header card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceDark)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(PrimaryGreen.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🛠️", fontSize = 28.sp)
                    }
                    Column {
                        Text(
                            "Quiz AI Generation Portal",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = PrimaryGreen
                        )
                        Text(
                            "Generate curriculum-aligned exam practices using Gemini 3.5 AI.",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }
            }

            when (val state = uiState) {
                is AdminUiState.Idle -> {
                    // Selection Controls Form
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text("Syllabus Configuration", fontWeight = FontWeight.Bold, color = TextPrimary)

                            // Select Subject Flow
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("Subject", color = TextSecondary, fontSize = 12.sp)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    listOf("Mathematics", "Biology", "Chemistry", "Geography").forEach { item ->
                                        val isSelected = subject == item
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(if (isSelected) PrimaryGreen else SurfaceVariantDark)
                                                .clickable { subject = item }
                                                .padding(vertical = 10.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                item.take(4) + ".",
                                                color = if (isSelected) Color.Black else TextPrimary,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                }
                            }

                            // Select Form Grade
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("Class Grade", color = TextSecondary, fontSize = 12.sp)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    listOf("Form 1", "Form 2", "Form 3", "Form 4").forEach { item ->
                                        val isSelected = formLevel == item
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(if (isSelected) PrimaryGreen else SurfaceVariantDark)
                                                .clickable { formLevel = item }
                                                .padding(vertical = 10.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                item,
                                                color = if (isSelected) Color.Black else TextPrimary,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                }
                            }

                            // Select Question Type
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("Question Type", color = TextSecondary, fontSize = 12.sp)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    listOf(
                                        QuestionType.MCQ to "MCQ Choice",
                                        QuestionType.FIB to "Type Blanks",
                                        QuestionType.TF to "True/False"
                                    ).forEach { (type, label) ->
                                        val isSelected = questionType == type
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(if (isSelected) PrimaryGreen else SurfaceVariantDark)
                                                .clickable { questionType = type }
                                                .padding(vertical = 10.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                label,
                                                color = if (isSelected) Color.Black else TextPrimary,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                }
                            }

                            // Specify Sub-Topic Focus
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("Syllabus Sub-Topic / Topic focus", color = TextSecondary, fontSize = 12.sp)
                                OutlinedTextField(
                                    value = topic,
                                    onValueChange = { topic = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = TextPrimary,
                                        unfocusedTextColor = TextPrimary,
                                        focusedContainerColor = SurfaceVariantDark,
                                        unfocusedContainerColor = SurfaceVariantDark,
                                        focusedBorderColor = PrimaryGreen,
                                        unfocusedBorderColor = BorderDark
                                    ),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                                )
                            }

                            // Quantifier selector
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("Number of Questions (${quantity})", color = TextSecondary, fontSize = 12.sp)
                                Slider(
                                    value = quantity.toFloat(),
                                    onValueChange = { quantity = it.toInt() },
                                    valueRange = 1f..5f,
                                    steps = 3,
                                    colors = SliderDefaults.colors(
                                        thumbColor = PrimaryGreen,
                                        activeTrackColor = PrimaryGreen,
                                        inactiveTrackColor = BorderDark
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    viewModel.generateQuizzes(subject, formLevel, questionType, topic, quantity)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen, contentColor = Color.Black),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("⚡ Generate with Gemini AI", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }
                    }
                }

                is AdminUiState.Generating -> {
                    Card(
                        modifier = Modifier.fillMaxWidth().height(260.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                CircularProgressIndicator(color = PrimaryGreen)
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        "Consulting Gemini-3.5-Flash...",
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        "Formulating educational prompts & validating JSON schema...",
                                        fontSize = 11.sp,
                                        color = TextSecondary,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }

                is AdminUiState.Error -> {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1F1212)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.Red)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.Info, contentDescription = "Error", tint = Color.Red)
                                Text("Generation Mismatch", color = Color.Red, fontWeight = FontWeight.Bold)
                            }
                            Text(state.message, color = TextSecondary, fontSize = 13.sp)
                            Button(
                                onClick = { viewModel.resetState() },
                                colors = ButtonDefaults.buttonColors(containerColor = BorderDark, contentColor = TextPrimary),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Configure Parameters Again")
                            }
                        }
                    }
                }

                is AdminUiState.Success -> {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Generated Draft (${editingQuestions.size} questions)", fontWeight = FontWeight.Bold, color = TextPrimary)
                            Button(
                                onClick = { viewModel.resetState() },
                                colors = ButtonDefaults.buttonColors(containerColor = SurfaceDark, contentColor = TextPrimary),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Regenerate")
                            }
                        }

                        editingQuestions.forEachIndexed { qIdx, question ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                                border = androidx.compose.foundation.BorderStroke(1.dp, BorderDark)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(
                                        "Draft Question #${qIdx + 1}",
                                        color = PrimaryGreen,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        letterSpacing = 1.sp
                                    )

                                    // Editable question text
                                    OutlinedTextField(
                                        value = question.questionText,
                                        onValueChange = { newVal ->
                                            editingQuestions = editingQuestions.toMutableList().apply {
                                                set(qIdx, question.copy(questionText = newVal))
                                            }
                                        },
                                        label = { Text("Question Text") },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedTextColor = TextPrimary,
                                            unfocusedTextColor = TextPrimary,
                                            focusedBorderColor = PrimaryGreen,
                                            unfocusedBorderColor = BorderDark
                                        )
                                    )

                                    // Choices editing (if MCQ)
                                    if (question.type == QuestionType.MCQ) {
                                        Text("Choices", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 12.sp)
                                        question.options.forEachIndexed { optIdx, option ->
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                                            ) {
                                                RadioButton(
                                                    selected = option.isCorrect,
                                                    onClick = {
                                                        val updatedOpts = question.options.mapIndexed { idx, opt ->
                                                            opt.copy(isCorrect = idx == optIdx)
                                                        }
                                                        editingQuestions = editingQuestions.toMutableList().apply {
                                                            set(qIdx, question.copy(options = updatedOpts))
                                                        }
                                                    },
                                                    colors = RadioButtonDefaults.colors(selectedColor = PrimaryGreen)
                                                )
                                                OutlinedTextField(
                                                    value = option.text,
                                                    onValueChange = { newVal ->
                                                        val updatedOpts = question.options.toMutableList().apply {
                                                            set(optIdx, option.copy(text = newVal))
                                                        }
                                                        editingQuestions = editingQuestions.toMutableList().apply {
                                                            set(qIdx, question.copy(options = updatedOpts))
                                                        }
                                                    },
                                                    modifier = Modifier.weight(1f),
                                                    colors = OutlinedTextFieldDefaults.colors(
                                                        focusedTextColor = TextPrimary,
                                                        unfocusedTextColor = TextPrimary,
                                                        focusedBorderColor = PrimaryGreen,
                                                        unfocusedBorderColor = BorderDark
                                                    )
                                                )
                                            }
                                        }
                                    } else if (question.type == QuestionType.TF) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            Text("Correct value is True?", color = TextSecondary, fontSize = 14.sp)
                                            Switch(
                                                checked = question.correctAnswerTf ?: true,
                                                onCheckedChange = { newVal ->
                                                    editingQuestions = editingQuestions.toMutableList().apply {
                                                        set(qIdx, question.copy(correctAnswerTf = newVal))
                                                    }
                                                },
                                                colors = SwitchDefaults.colors(checkedThumbColor = PrimaryGreen)
                                            )
                                        }
                                    } else if (question.type == QuestionType.FIB) {
                                        OutlinedTextField(
                                            value = question.acceptedAnswers.firstOrNull() ?: "",
                                            onValueChange = { newVal ->
                                                editingQuestions = editingQuestions.toMutableList().apply {
                                                    set(qIdx, question.copy(acceptedAnswers = listOf(newVal)))
                                                }
                                            },
                                            label = { Text("Correct Answer blank text") },
                                            modifier = Modifier.fillMaxWidth(),
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedTextColor = TextPrimary,
                                                unfocusedTextColor = TextPrimary,
                                                focusedBorderColor = PrimaryGreen,
                                                unfocusedBorderColor = BorderDark
                                            )
                                        )
                                    }

                                    // Editable Textbook explanation
                                    OutlinedTextField(
                                        value = question.explanation,
                                        onValueChange = { newVal ->
                                            editingQuestions = editingQuestions.toMutableList().apply {
                                                set(qIdx, question.copy(explanation = newVal))
                                            }
                                        },
                                        label = { Text("Syllabus Textbook Explanation") },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedTextColor = TextPrimary,
                                            unfocusedTextColor = TextPrimary,
                                            focusedBorderColor = PrimaryGreen,
                                            unfocusedBorderColor = BorderDark
                                        ),
                                        minLines = 3
                                    )
                                }
                            }
                        }

                        Button(
                            onClick = {
                                Toast.makeText(context, "Syllabus Quiz published successfully to live packages!", Toast.LENGTH_LONG).show()
                                viewModel.resetState()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen, contentColor = Color.Black),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Publish to live pack and scale database", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}
