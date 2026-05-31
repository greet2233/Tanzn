package com.example.ui.screens.study

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.domain.models.McqOption
import com.example.domain.models.QuestionType
import com.example.ui.components.AppButton
import com.example.ui.components.MathStepRenderer
import com.example.ui.components.WrongAnswerReviewCard
import com.example.ui.theme.*
import com.example.ui.viewmodels.QuizViewModel
import kotlinx.coroutines.launch

@Composable
fun QuizIntroScreen(
    packId: String,
    quizType: QuestionType,
    viewModel: QuizViewModel,
    onNavigateBack: () -> Unit,
    onStartQuiz: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(PrimaryGreen.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                when (quizType) {
                    QuestionType.MCQ -> "📝"
                    QuestionType.FIB -> "✍️"
                    QuestionType.TF -> "✅"
                }, fontSize = 40.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "${quizType.name} Challenge",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Test your knowledge and earn XP! Are you ready to begin?",
            fontSize = 16.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        AppButton(
            text = "Start Quiz",
            onClick = {
                if (viewModel.checkAccess(quizType)) {
                    viewModel.startQuiz(packId, quizType)
                    onStartQuiz()
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavigateBack) {
            Text("Cancel", color = TextSecondary, fontSize = 16.sp)
        }
    }

    if (state.isShowingLimitModal) {
        DailyLimitModal(onDismiss = { viewModel.dismissLimitModal() })
    }
}

@Composable
fun DailyLimitModal(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceDark,
        titleContentColor = TextPrimary,
        textContentColor = TextSecondary,
        title = {
            Text("Daily Limit Reached", fontWeight = FontWeight.Bold)
        },
        text = {
            Text("You have reached your daily limit for this question type. Upgrade to Premium for unlimited access and more features!")
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Text("Upgrade to Premium", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Maybe Later", color = TextSecondary)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizQuestionScreen(
    viewModel: QuizViewModel,
    onNavigateToResult: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val question = state.currentQuestion
    val attempt = state.attempt

    if (question == null || attempt == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = PrimaryGreen)
        }
    } else {
        var selectedMcqOption by remember { mutableStateOf<String?>(null) }
        var fibInput by remember { mutableStateOf("") }
        var selectedTf by remember { mutableStateOf<Boolean?>(null) }
    
        // Reset local state on question change
        LaunchedEffect(question.id) {
            selectedMcqOption = null
            fibInput = ""
            selectedTf = null
        }
    
        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(SurfaceDark)
                            .clickable(onClick = onNavigateBack),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("×", color = TextPrimary, fontSize = 24.sp)
                    }
                    
                    // Progress Bar
                    val progress = if (attempt.totalQuestions > 0) {
                        (attempt.currentQuestionIndex + 1).toFloat() / attempt.totalQuestions
                    } else {
                        0f
                    }
                    Box(modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                        .height(8.dp)
                        .clip(CircleShape)
                        .background(SurfaceDark)
                    ) {
                        Box(modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(progress)
                            .clip(CircleShape)
                            .background(PrimaryGreen)
                        )
                    }
    
                    Text(
                        "${attempt.currentQuestionIndex + 1}/${attempt.totalQuestions}",
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(20.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
    
                // Question Text (MathRenderer Placeholder)
                Text(
                    text = question.questionText,
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 32.sp
                )
    
                Spacer(modifier = Modifier.height(32.dp))
    
                // Inputs based on type
                when (question.type) {
                    QuestionType.MCQ -> {
                        question.options.forEach { option ->
                            McqOptionCard(
                                option = option,
                                isSelected = selectedMcqOption == option.id,
                                onClick = { selectedMcqOption = option.id }
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                    QuestionType.FIB -> {
                        OutlinedTextField(
                            value = fibInput,
                            onValueChange = { fibInput = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Type your answer...", color = TextSecondary) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedBorderColor = PrimaryGreen,
                                unfocusedBorderColor = BorderDark,
                                focusedContainerColor = SurfaceDark,
                                unfocusedContainerColor = SurfaceDark
                            ),
                            singleLine = true
                        )
                    }
                    QuestionType.TF -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            TfButton(
                                text = "True",
                                isSelected = selectedTf == true,
                                onClick = { selectedTf = true },
                                modifier = Modifier.weight(1f)
                            )
                            TfButton(
                                text = "False",
                                isSelected = selectedTf == false,
                                onClick = { selectedTf = false },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
    
                Spacer(modifier = Modifier.weight(1f))
    
                val isAnswerGiven = when (question.type) {
                    QuestionType.MCQ -> selectedMcqOption != null
                    QuestionType.FIB -> fibInput.isNotBlank()
                    QuestionType.TF -> selectedTf != null
                }
    
                AppButton(
                    text = "Check Answer",
                    onClick = {
                        when (question.type) {
                            QuestionType.MCQ -> viewModel.checkAnswerMcq(selectedMcqOption!!)
                            QuestionType.FIB -> viewModel.checkAnswerFib(fibInput)
                            QuestionType.TF -> viewModel.checkAnswerTf(selectedTf!!)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isAnswerGiven
                )
             if (state.isFeedbackVisible) {
                FeedbackModal(
                    viewModel = viewModel, 
                    onNavigateToResult = onNavigateToResult
                )
            }
        }
    }
}
}


@Composable
fun McqOptionCard(option: McqOption, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) PrimaryGreen.copy(alpha = 0.2f) else SurfaceDark)
            .border(
                2.dp,
                if (isSelected) PrimaryGreen else BorderDark,
                RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .border(2.dp, if (isSelected) PrimaryGreen else TextSecondary, CircleShape)
                .background(if (isSelected) PrimaryGreen else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Text("✓", color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = option.text,
            color = TextPrimary,
            fontSize = 16.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun TfButton(text: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) PrimaryGreen.copy(alpha = 0.2f) else SurfaceDark)
            .border(
                2.dp,
                if (isSelected) PrimaryGreen else BorderDark,
                RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = TextPrimary,
            fontSize = 20.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold
        )
    }
}

@Composable
fun FeedbackModal(
    viewModel: QuizViewModel, 
    onNavigateToResult: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val isCorrect = state.feedbackIsCorrect
    val color = if (isCorrect) PrimaryGreen else SecondaryOrange
    val title = if (isCorrect) "Awesome!" else "Good try!"
    val icon = if (isCorrect) "🎉" else "💪"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(icon, fontSize = 32.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
                Spacer(modifier = Modifier.weight(1f))
                if (isCorrect && state.earnedXpThisQuestion > 0) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(PrimaryGreen.copy(alpha = 0.2f))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            "+${state.earnedXpThisQuestion} XP",
                            color = PrimaryGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            
            if (!isCorrect) {
                Text(
                    "Correct Answer: ${state.feedbackCorrectAnswer}",
                    color = PrimaryGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            MathStepRenderer(
                text = state.feedbackExplanation,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            AppButton(
                text = "Continue",
                onClick = { viewModel.nextQuestion(onComplete = onNavigateToResult) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun QuizResultScreen(
    viewModel: QuizViewModel,
    onNavigateBack: () -> Unit,
    onNavigateNext: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val attempt = state.attempt
    if (attempt == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = PrimaryGreen)
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .verticalScroll(androidx.compose.foundation.rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(PrimaryGreen.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🏆", fontSize = 50.sp)
                }
        
                Spacer(modifier = Modifier.height(32.dp))
        
                Text("Quiz Completed!", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text("You did a great job.", color = TextSecondary, fontSize = 16.sp)
        
                Spacer(modifier = Modifier.height(32.dp))
        
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(modifier = Modifier.weight(1f).padding(end = 4.dp)) { ResultStatCard("Score", "${attempt.scorePercent}%") }
                    Box(modifier = Modifier.weight(1f).padding(horizontal = 4.dp)) { ResultStatCard("XP Earned", "+${attempt.xpEarned}") }
                    Box(modifier = Modifier.weight(1f).padding(start = 4.dp)) { ResultStatCard("Correct", "${attempt.correctCount}/${attempt.totalQuestions}") }
                }
        
                Spacer(modifier = Modifier.height(32.dp))
                
                // Learn from mistakes section
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Learn from Mistakes",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    
                    if (state.wrongQuestions.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(PrimaryGreen.copy(alpha = 0.05f))
                                .border(1.dp, PrimaryGreen.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                                .padding(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("🌟", fontSize = 28.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "Perfect Score!",
                                    color = PrimaryGreen,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "You answered every question correctly. Excellent job, scholar!",
                                    color = TextSecondary,
                                    fontSize = 13.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        state.wrongQuestions.forEach { wrongQ ->
                            val correctOptionStr = when (wrongQ.type) {
                                com.example.domain.models.QuestionType.MCQ -> wrongQ.options.find { it.isCorrect }?.text ?: "Unknown"
                                com.example.domain.models.QuestionType.FIB -> wrongQ.acceptedAnswers.firstOrNull() ?: "Unknown"
                                com.example.domain.models.QuestionType.TF -> wrongQ.correctAnswerTf.toString()
                             }
                            
                            com.example.ui.components.WrongAnswerReviewCard(
                                questionText = wrongQ.questionText,
                                correctAnswer = correctOptionStr,
                                explanation = wrongQ.explanation
                            )
                        }
                    }
                }
        
                Spacer(modifier = Modifier.height(40.dp))
        
                AppButton(
                    text = "Back to Learning Pack",
                    onClick = onNavigateBack,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun ResultStatCard(title: String, value: String) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceDark)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = PrimaryGreen)
        Spacer(modifier = Modifier.height(4.dp))
        Text(title, fontSize = 12.sp, color = TextSecondary)
    }
}
