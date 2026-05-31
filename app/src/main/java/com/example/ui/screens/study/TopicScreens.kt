package com.example.ui.screens.study

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.domain.models.LearningPack
import com.example.domain.models.Topic
import com.example.domain.models.QuestionType
import com.example.ui.theme.*
import com.example.ui.components.AppButton
import com.example.ui.components.PremiumLockCard
import com.example.ui.viewmodels.CurriculumViewModel

@Composable
fun TopicScreen(
// ... Keeping as it is. We will use edit_file on specific blocks instead. Let's do multi_edit

    subjectId: String,
    subjectName: String,
    viewModel: CurriculumViewModel = viewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToPackList: (String, String) -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(subjectId) {
        viewModel.loadTopics(subjectId)
    }

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(
                modifier = Modifier.size(40.dp).clip(CircleShape).background(SurfaceDark).clickable(onClick = onNavigateBack),
                contentAlignment = Alignment.Center
            ) {
                Text("←", color = TextPrimary, fontSize = 20.sp)
            }
            Text(subjectName, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        val topics = state.topics
        
        if (topics.isEmpty()) {
            Text("No topics available.", color = TextSecondary, modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp), contentPadding = PaddingValues(bottom = 100.dp)) {
                items(topics.size) { index ->
                    TopicCard(topics[index]) { 
                        onNavigateToPackList(topics[index].id, topics[index].name) 
                    }
                }
            }
        }
    }
}

@Composable
fun TopicCard(topic: Topic, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(SurfaceDark)
            .border(1.dp, BorderDark, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(topic.name, color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("${topic.packCount} Learning Packs", color = TextSecondary, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Difficulty: ${topic.difficulty}", color = PrimaryGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
        Text("▶", fontSize = 16.sp, color = PrimaryGreen)
    }
}

@Composable
fun PackListScreen(
    topicId: String,
    topicName: String,
    subjectId: String,
    viewModel: CurriculumViewModel = viewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (String, String) -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(topicId) {
        viewModel.loadLearningPacks(topicId, subjectId)
    }

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(
                modifier = Modifier.size(40.dp).clip(CircleShape).background(SurfaceDark).clickable(onClick = onNavigateBack),
                contentAlignment = Alignment.Center
            ) {
                Text("←", color = TextPrimary, fontSize = 20.sp)
            }
            Text(topicName, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        val packs = state.learningPacks
        
        if (packs.isEmpty()) {
            Text("No learning packs available.", color = TextSecondary, modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp), contentPadding = PaddingValues(bottom = 100.dp)) {
                items(packs.size) { index ->
                    LearningPackListCard(packs[index]) { 
                        onNavigateToDetail(packs[index].id, packs[index].title) 
                    }
                }
            }
        }
    }
}

@Composable
fun LearningPackListCard(pack: LearningPack, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(SurfaceDark)
            .border(1.dp, BorderDark, RoundedCornerShape(20.dp))
            .clickable(enabled = !pack.isPremiumLocked, onClick = onClick)
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(pack.title, color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("${pack.estimatedMinutes} Mins · ${pack.difficulty}", color = TextSecondary, fontSize = 14.sp)
            if (pack.isPremiumLocked) {
                Spacer(modifier = Modifier.height(4.dp))
                Text("Premium Only", color = SecondaryOrange, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
        
        if (pack.isPremiumLocked) {
            Text("🔒", fontSize = 20.sp)
        } else {
            Text("▶", fontSize = 16.sp, color = PrimaryGreen)
        }
    }
}

@Composable
fun LearningPackScreen(
    packId: String,
    packName: String,
    viewModel: CurriculumViewModel = viewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToQuizIntro: (String, String) -> Unit,
    onShowPremiumModal: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val pack = state.learningPacks.find { it.id == packId }
    
    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(
                modifier = Modifier.size(40.dp).clip(CircleShape).background(SurfaceDark).clickable(onClick = onNavigateBack),
                contentAlignment = Alignment.Center
            ) {
                Text("←", color = TextPrimary, fontSize = 20.sp)
            }
            // Title
            val title = packName.takeIf { it.length <= 20 } ?: "${packName.take(17)}..."
            Text(title, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (pack != null) {
            Text(pack.description, color = TextSecondary, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                XPBadge(pack.completionXP)
                DifficultyBadge(pack.difficulty)
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                PremiumLockCard(title = "Topic Summary", subtitle = "Unlock clear AI explanations", onClick = onShowPremiumModal)
            }
            item {
                PackItemCard("Multiple Choice (MCQ)", "10 Questions", "5 left today", icon = "📝") {
                    onNavigateToQuizIntro(packId, QuestionType.MCQ.name)
                }
            }
            item {
                PackItemCard("Fill in Blanks (FIB)", "5 Questions", "3 left today", icon = "✏️") {
                    onNavigateToQuizIntro(packId, QuestionType.FIB.name)
                }
            }
            item {
                PackItemCard("True / False (TF)", "5 Questions", "2 left today", icon = "✅") {
                    onNavigateToQuizIntro(packId, QuestionType.TF.name)
                }
            }
            item {
                PremiumLockCard(title = "Higher Order Questions", subtitle = "Premium Exam Practice", onClick = onShowPremiumModal)
            }
        }
    }
}

@Composable
fun XPBadge(xp: Int) {
    Box(
        modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(PrimaryGreen.copy(alpha = 0.2f)).border(1.dp, PrimaryGreen, RoundedCornerShape(8.dp)).padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text("+$xp XP", color = PrimaryGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun DifficultyBadge(difficulty: String) {
    val color = when(difficulty) {
        "Beginner" -> PrimaryGreen
        "Intermediate" -> SecondaryOrange
        else -> Color.Red
    }
    Box(
        modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(color.copy(alpha = 0.2f)).border(1.dp, color, RoundedCornerShape(8.dp)).padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(difficulty, color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun PackItemCard(title: String, subtitle: String, extraInfo: String = "", icon: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(SurfaceDark)
            .border(1.dp, BorderDark, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFF1E293B)), contentAlignment = Alignment.Center) {
                Text(icon, fontSize = 20.sp)
            }
            Column {
                Text(title, color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(subtitle, color = TextSecondary, fontSize = 12.sp)
                    if (extraInfo.isNotEmpty()) {
                        Text("• $extraInfo", color = PrimaryGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        Text("▶", fontSize = 16.sp, color = PrimaryGreen)
    }
}

@Composable
fun PackCompletionScreen(score: Int, maxScore: Int, onFinish: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(contentAlignment = Alignment.Center) {
            // Confetti placeholder
            Text("✨🎊✨", fontSize = 80.sp)
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text("Awesome Job!", fontSize = 36.sp, fontWeight = FontWeight.Black, color = TextPrimary)
        Text("You mastered this topic.", color = TextSecondary, fontSize = 16.sp)
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(PrimaryGreen.copy(alpha = 0.1f))
                    .border(2.dp, PrimaryGreen, RoundedCornerShape(24.dp))
                    .padding(horizontal = 32.dp, vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("+100", fontSize = 48.sp, fontWeight = FontWeight.Black, color = PrimaryGreen)
                    Text("XP EARNED", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = PrimaryGreen, letterSpacing = 2.sp)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFFFFB800).copy(alpha = 0.1f))
                    .border(2.dp, Color(0xFFFFB800), RoundedCornerShape(24.dp))
                    .padding(horizontal = 32.dp, vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("💎", fontSize = 24.sp)
                    Text("+20 Coins", fontSize = 24.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFB800))
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.background(SurfaceDark, RoundedCornerShape(16.dp)).padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text("🔥", fontSize = 24.sp)
            Column {
                Text("15 Day Streak!", color = SecondaryOrange, fontWeight = FontWeight.Bold)
                Text("You're on fire today.", color = TextSecondary, fontSize = 12.sp)
            }
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        
        AppButton(text = "CONTINUE", onClick = onFinish)
        Spacer(modifier = Modifier.height(16.dp))
        AppButton(text = "SHARE ACHIEVEMENT", onClick = {}, isSecondary = true)
    }
}
