package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.models.AiExplanation
import com.example.ui.theme.*

/**
 * Renders mathematical formulas styled cleanly like a textbook page.
 * Safely parses and displays \\[ ... \\] or \\( ... \\) equations beautifully.
 */
@Composable
fun MathStepRenderer(text: String, modifier: Modifier = Modifier) {
    val mathBlocks = remember(text) { parseMathText(text) }
    
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        mathBlocks.forEach { block ->
            if (block.isFormula) {
                // Formula display card: Emerald/Gold highlighted boxed math representation
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF0F172A))
                        .border(1.dp, PrimaryGreen.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .padding(vertical = 12.dp, horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = block.content.trim(),
                        color = PrimaryGreen,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                Text(
                    text = block.content,
                    color = TextPrimary,
                    fontSize = 15.sp,
                    lineHeight = 22.sp
                )
            }
        }
    }
}

data class ParserBlock(val content: String, val isFormula: Boolean)

private fun parseMathText(input: String): List<ParserBlock> {
    val list = mutableListOf<ParserBlock>()
    var remaining = input
    
    while (remaining.isNotEmpty()) {
        val startBlockIdx = remaining.indexOf("\\[")
        val startInlineIdx = remaining.indexOf("\\(")
        
        // Find which marker comes first
        val firstStartIdx = when {
            startBlockIdx >= 0 && startInlineIdx >= 0 -> minOf(startBlockIdx, startInlineIdx)
            startBlockIdx >= 0 -> startBlockIdx
            else -> startInlineIdx
        }
        
        if (firstStartIdx < 0) {
            list.add(ParserBlock(remaining, false))
            break
        }
        
        // Add text before standard formula
        if (firstStartIdx > 0) {
            list.add(ParserBlock(remaining.substring(0, firstStartIdx), false))
        }
        
        val isBlock = remaining.startsWith("\\[", firstStartIdx)
        val endMarker = if (isBlock) "\\]" else "\\)"
        
        val endIdx = remaining.indexOf(endMarker, firstStartIdx + 2)
        if (endIdx < 0) {
            // Unclosed mathematical tag, return as normal text
            list.add(ParserBlock(remaining.substring(firstStartIdx), false))
            break
        }
        
        val formulaRaw = remaining.substring(firstStartIdx + 2, endIdx)
        list.add(ParserBlock(formulaRaw, true))
        remaining = remaining.substring(endIdx + 2)
    }
    
    return list
}

@Composable
fun WrongAnswerReviewCard(
    questionText: String,
    correctAnswer: String,
    explanation: String,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    AppCard(modifier = modifier.fillMaxWidth(), borderColor = SecondaryOrange.copy(alpha = 0.4f)) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(SecondaryOrange.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("❌", fontSize = 14.sp)
                }
                Text("Needs Review", color = SecondaryOrange, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            
            Text(
                text = questionText,
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            
            Text(
                text = "Correct Answer: $correctAnswer",
                color = PrimaryGreen,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceVariantDark)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Textbook Explanation",
                        color = PrimaryGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 0.5.sp
                    )
                    MathStepRenderer(text = explanation)
                }
            }
            
            Button(
                onClick = { expanded = !expanded },
                colors = ButtonDefaults.buttonColors(containerColor = SurfaceDark, contentColor = TextPrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (expanded) "🔼 Hide Explanation" else "💡 View Explanation")
            }
        }
    }
}

