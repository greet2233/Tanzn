package com.example.ui.screens.onboarding

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay

// Premium colors
val DeepIndigoBackground = Color(0xFF0F172A)
val GoldColor = Color(0xFFEAB308)
val GoldGlowColor = Color(0x33EAB308)

@Composable
fun SplashScreen(onNavigateNext: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2000)
        onNavigateNext()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF020617), Color(0xFF0F172A))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Subtle background glow
        Box(
            modifier = Modifier
                .size(300.dp)
                .background(Brush.radialGradient(colors = listOf(Color(0x1F38BDF8), Color.Transparent)))
                .blur(50.dp)
        )
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "🎓",
                fontSize = 72.sp
            )
            Text(
                "SOMANECTA",
                color = TextPrimary,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 6.sp
            )
            Text(
                "NECTA SYLLABUS HUB",
                color = GoldColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp
            )
        }
    }
}

@Composable
fun WelcomeScreen(onNavigateNext: () -> Unit) {
    var selectedForm by remember { mutableStateOf("Form 2") }
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepIndigoBackground)
    ) {
        // Deep atmosphere glows
        Box(
            modifier = Modifier
                .size(400.dp)
                .align(Alignment.TopEnd)
                .offset(x = 100.dp, y = (-100).dp)
                .background(Brush.radialGradient(colors = listOf(Color(0x1A0EA5E9), Color.Transparent)))
                .blur(60.dp)
        )
        Box(
            modifier = Modifier
                .size(350.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-100).dp, y = 100.dp)
                .background(Brush.radialGradient(colors = listOf(Color(0x11EAB308), Color.Transparent)))
                .blur(60.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            
            // Header
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Learn Today. Excel Tomorrow.",
                    color = TextPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center,
                    lineHeight = 36.sp
                )
                Text(
                    text = "NECTA-aligned learning for Form 1–4 students.",
                    color = TextSecondary,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }

            // Trust Badges
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf("NECTA Aligned", "Curriculum Based", "Exam Focused").forEach { badge ->
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0x331E293B))
                            .border(1.dp, Color(0x33F8FAFC), CircleShape)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = badge,
                            color = TextPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Premium Academic Illustration
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0x661E293B)),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color(0x22F8FAFC))
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    // Back circular glow
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Brush.radialGradient(colors = listOf(Color(0x1F38BDF8), Color.Transparent)))
                    )
                    
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("📚", fontSize = 48.sp)
                            Text("⭐", fontSize = 36.sp)
                            Text("🎓", fontSize = 48.sp)
                        }
                        Text(
                            "Tanzania O-Level Syllabus",
                            color = GoldColor,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp
                        )
                    }
                }
            }

            // Platform selection Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color(0xFF334155))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column {
                        Text(
                            text = "Choose Your Form",
                            color = TextPrimary,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "We’ll personalize your learning journey.",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }

                    // Form selective grid (Form 1 - Form 4)
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        val forms = listOf("Form 1", "Form 2", "Form 3", "Form 4")
                        forms.chunked(2).forEach { pair ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                pair.forEach { form ->
                                    val isSelected = selectedForm == form
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(if (isSelected) Color(0x33EAB308) else Color(0x0F000000))
                                            .border(
                                                width = if (isSelected) 2.dp else 1.dp,
                                                color = if (isSelected) GoldColor else Color(0xFF334155),
                                                shape = RoundedCornerShape(16.dp)
                                            )
                                            .clickable { selectedForm = form }
                                            .padding(vertical = 16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(
                                                text = form,
                                                color = if (isSelected) GoldColor else TextPrimary,
                                                fontWeight = FontWeight.Black,
                                                fontSize = 16.sp
                                            )
                                            Text(
                                                text = when (form) {
                                                    "Form 1" -> "O-Level Physics/Math"
                                                    "Form 2" -> "NECTA Assessment"
                                                    "Form 3" -> "Core Syllabus"
                                                    else -> "National Finals"
                                                },
                                                color = if (isSelected) GoldColor.copy(alpha = 0.8f) else TextSecondary,
                                                fontSize = 9.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Action Button
            Button(
                onClick = onNavigateNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldColor,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    "Get Started",
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp
                )
            }

            // Footer
            Text(
                "Trusted by students across Tanzania",
                color = TextSecondary,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
