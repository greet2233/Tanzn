package com.example.ui.screens.onboarding

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PrimaryGreen
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.SecondaryOrange

@Composable
fun OtpLoginScreen(onNavigateNext: (String) -> Unit) {
    var phoneInput by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("Student") }
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
    ) {
        // Deep indigo ambient glow
        Box(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.TopStart)
                .offset(x = (-50).dp, y = (-50).dp)
                .background(Brush.radialGradient(colors = listOf(Color(0x2238BDF8), Color.Transparent)))
                .blur(40.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(scrollState)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            
            // Back navigation & Help Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0x331E293B))
                        .clickable { /* Back step is handled via navigation */ },
                    contentAlignment = Alignment.Center
                ) {
                    Text("←", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0x331E293B))
                        .clickable { /* Help info modal */ },
                    contentAlignment = Alignment.Center
                ) {
                    Text("❓", color = Color(0xFFEAB308), fontSize = 16.sp)
                }
            }

            // Screen Header Titles
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Welcome Back!",
                    color = TextPrimary,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "Sign in to continue your learning journey.",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            }

            // Role segmented selector tabs
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color(0xFF334155))
            ) {
                Row(
                    modifier = Modifier.padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val roles = listOf("Student", "Parent", "Teacher")
                    roles.forEach { role ->
                        val isSelected = selectedRole == role
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) Color(0xFFEAB308) else Color.Transparent)
                                .clickable { selectedRole = role }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = role,
                                color = if (isSelected) Color.Black else TextSecondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            // Phone number Input Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, Color(0xFF334155))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "Enter Phone Number",
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF0F172A))
                            .border(1.dp, Color(0xFF334155), RoundedCornerShape(12.dp))
                            .padding(horizontal = 14.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Flag mockup & code
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("🇹🇿", fontSize = 18.sp)
                            Text("+255", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                        
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(20.dp)
                                .background(Color(0xFF334155))
                        )

                        // Editable placeholder input flow
                        BasicTextFieldDummy(
                            value = phoneInput,
                            onValueChange = { if (it.length <= 9) phoneInput = it },
                            placeholder = "7XX XXX XXX"
                        )
                    }

                    Text(
                        "We’ll send you a one-time password (OTP).",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }

            // Auto-populated simulation code block to show '1 2 3 4 5 6' demo for UX showcase
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B).copy(alpha = 0.5f)),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, Color(0x1F334155))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        "Enter OTP",
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        "We’ve simulated sending a 6-digit code to +255 ${phoneInput.ifBlank { "7XX XXX XXX" }}",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )

                    // 6 digits box representation
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val demoDigits = listOf("1", "2", "3", "4", "5", "6")
                        demoDigits.forEach { dig ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF0F172A))
                                    .border(1.dp, Color(0xFF475569), RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = dig,
                                    color = Color(0xFFEAB308),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Resend OTP in 00:45",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            "Edit Number",
                            color = Color(0xFF38BDF8),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { /* Reset flow */ }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Action verify trigger
            Button(
                onClick = { onNavigateNext("255" + phoneInput.ifBlank { "712345678" }) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEAB308),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    "Verify & Continue",
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp
                )
            }

            // Alternative Single Sign On integrations
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("or sign in containing", color = TextSecondary, fontSize = 12.sp)
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    listOf("Google 🌐", "Apple 🍎", "Email ✉️").forEach { platform ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF1E293B))
                                .border(1.dp, Color(0xFF334155), RoundedCornerShape(12.dp))
                                .clickable { onNavigateNext("sso_demo") }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = platform,
                                color = TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Security assurance footer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("🛡️ ", fontSize = 14.sp)
                Text(
                    "Your data is secure and encrypted",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun BasicTextFieldDummy(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp)
    ) {
        if (value.isEmpty()) {
            Text(placeholder, color = TextSecondary.copy(alpha = 0.5f), fontSize = 15.sp)
        }
        androidx.compose.foundation.text.BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = TextPrimary, fontWeight = FontWeight.Bold),
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            cursorBrush = Brush.verticalGradient(listOf(Color(0xFFEAB308), Color(0xFFEAB308)))
        )
    }
}

@Composable
fun OtpVerificationScreen(onNavigateNext: () -> Unit) {
    // Both states merged into screen login flow above to meet beautiful 3-screen high quality standard.
    // Kept to fulfill router references.
    LaunchedEffect(Unit) {
        onNavigateNext()
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = Color(0xFFEAB308))
    }
}
