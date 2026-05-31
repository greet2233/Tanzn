package com.example.ui.screens.study

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.domain.models.Subject
import com.example.ui.theme.*
import com.example.ui.viewmodels.CurriculumViewModel

@Composable
fun SubjectsScreen(
    onNavigateToTopic: (String, String) -> Unit,
    onNavigateToSubscription: () -> Unit = {},
    viewModel: CurriculumViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Your", fontSize = 24.sp, color = TextPrimary)
                Text("Subjects", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = PrimaryGreen)
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        // Form Selector
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(state.forms) { form ->
                val isSelected = state.selectedForm?.id == form.id
                FormSelectorChip(
                    label = form.name,
                    isSelected = isSelected,
                    onClick = { viewModel.selectForm(form.id) }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search subjects...", color = TextSecondary) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryGreen,
                unfocusedBorderColor = BorderDark,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                unfocusedContainerColor = SurfaceDark,
                focusedContainerColor = SurfaceDark
            ),
            shape = RoundedCornerShape(16.dp)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp), contentPadding = PaddingValues(bottom = 100.dp)) {
            val queryFilteredSubjects = state.subjects.filter {
                it.name.contains(searchQuery, ignoreCase = true)
            }
            if (queryFilteredSubjects.isEmpty()) {
                item {
                    Text("No subjects found.", color = TextSecondary, modifier = Modifier.padding(16.dp))
                }
            } else {
                items(queryFilteredSubjects.size) { index ->
                    val subject = queryFilteredSubjects[index]
                    SubjectCard(
                        subject = subject,
                        onPremiumClick = onNavigateToSubscription,
                        onClick = {
                            onNavigateToTopic(subject.id, subject.name)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FormSelectorChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) PrimaryGreen else SurfaceDark)
            .border(
                1.dp,
                if (isSelected) PrimaryGreen else BorderDark,
                RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (isSelected) Color.Black else TextSecondary,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

@Composable
fun SubjectCard(subject: Subject, onPremiumClick: () -> Unit, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(SurfaceDark)
            .border(1.dp, BorderDark, RoundedCornerShape(24.dp))
            .clickable(onClick = {
                if (subject.isPremiumLocked) {
                    onPremiumClick()
                } else {
                    onClick()
                }
            })
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(android.graphics.Color.parseColor(subject.colorHex)).copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(subject.icon, fontSize = 24.sp)
            }
            Column {
                Text(subject.name, color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(4.dp))
                if (subject.isPremiumLocked) {
                    Text("Premium only", color = SecondaryOrange, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(
                            modifier = Modifier.width(100.dp).height(6.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.1f))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(subject.progress / 100f)
                                    .fillMaxHeight()
                                    .clip(CircleShape)
                                    .background(PrimaryGreen)
                            )
                        }
                        Text("${subject.progress}%", color = PrimaryGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        
        if (subject.isPremiumLocked) {
            Text("🔒", fontSize = 20.sp)
        } else {
            Text("▶", fontSize = 16.sp, color = TextSecondary)
        }
    }
}
