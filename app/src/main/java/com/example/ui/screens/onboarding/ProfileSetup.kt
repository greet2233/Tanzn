package com.example.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BorderDark
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun CreateProfileScreen(onNavigateNext: () -> Unit) {
    var fullName by remember { mutableStateOf("Amani") }
    var formNum by remember { mutableStateOf("4") }
    var schoolName by remember { mutableStateOf("Jangwani Secondary School") }

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(24.dp),
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text("Setup", fontSize = 24.sp, color = TextPrimary)
        Text("Your Profile", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Box(
            modifier = Modifier.size(96.dp).clip(CircleShape).background(SurfaceDark).align(Alignment.CenterHorizontally).border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("📷", fontSize = 32.sp)
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name", color = TextSecondary) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = BorderDark,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            shape = RoundedCornerShape(12.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = formNum,
            onValueChange = { formNum = it },
            label = { Text("Form (1-4)", color = TextSecondary) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = BorderDark,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            shape = RoundedCornerShape(12.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = schoolName,
            onValueChange = { schoolName = it },
            label = { Text("School Name", color = TextSecondary) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = BorderDark,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            shape = RoundedCornerShape(12.dp)
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        Button(
            onClick = onNavigateNext,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("SAVE PROFILE", fontWeight = FontWeight.Black)
        }
    }
}

@Composable
fun SubjectSelectionScreen(onNavigateNext: () -> Unit) {
    val subjects = listOf("Mathematics", "Biology", "Chemistry", "Physics", "English", "Geography")
    val selectedSubjects = remember { mutableStateListOf("Mathematics", "Biology", "Physics") }

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(24.dp),
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text("Choose Your", fontSize = 24.sp, color = TextPrimary)
        Text("Subjects", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Select subjects you want to focus on.", color = TextSecondary)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            subjects.forEach { subject ->
                val isSelected = selectedSubjects.contains(subject)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f) else SurfaceDark)
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else BorderDark,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable {
                            if (isSelected) {
                                selectedSubjects.remove(subject)
                            } else {
                                selectedSubjects.add(subject)
                            }
                        }
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(subject, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                            .border(2.dp, if (isSelected) MaterialTheme.colorScheme.primary else BorderDark, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Text("✓", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        Button(
            onClick = onNavigateNext,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("START LEARNING", fontWeight = FontWeight.Black)
        }
    }
}
