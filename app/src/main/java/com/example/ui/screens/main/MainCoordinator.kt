package com.example.ui.screens.main

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
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
import com.example.DashboardScreen
import com.example.ui.screens.study.SubjectsScreen
import com.example.ui.theme.*
import com.example.ui.viewmodels.CurriculumViewModel

enum class MainTab { HOME, SUBJECTS, QUIZ, LEADERBOARD, PROFILE }

@Composable
fun MainCoordinatorScreen(
    curriculumViewModel: CurriculumViewModel,
    gamificationViewModel: com.example.ui.viewmodels.GamificationViewModel,
    onNavigateToTopic: (String, String) -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToSubscription: () -> Unit,
    onNavigateToMissions: () -> Unit,
    onNavigateToRewards: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(MainTab.HOME) }
    
    Scaffold(
        containerColor = Color(0xFF0F172A),
        bottomBar = {
            BottomNavBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            when (selectedTab) {
                MainTab.HOME -> DashboardScreen(
                    onNavigateToSubscription = onNavigateToSubscription,
                    onNavigateToMissions = onNavigateToMissions,
                    onNavigateToStudy = { selectedTab = MainTab.SUBJECTS },
                    gamificationViewModel = gamificationViewModel
                )
                MainTab.SUBJECTS -> SubjectsScreen(onNavigateToTopic, onNavigateToSubscription, curriculumViewModel)
                MainTab.QUIZ -> StudentQuizHubScreen(onNavigateToSubscription)
                MainTab.LEADERBOARD -> LeaderboardScreen(gamificationViewModel = gamificationViewModel)
                MainTab.PROFILE -> ProfileScreen(
                    onNavigateToSettings = onNavigateToSettings,
                    onNavigateToSubscription = onNavigateToSubscription,
                    onNavigateToRewards = onNavigateToRewards,
                    gamificationViewModel = gamificationViewModel
                )
            }
        }
    }
}

@Composable
fun BottomNavBar(selectedTab: MainTab, onTabSelected: (MainTab) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 20.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .clip(CircleShape)
                .background(SurfaceDark.copy(alpha = 0.9f))
                .border(1.dp, Color.White.copy(alpha = 0.08f), CircleShape)
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(icon = "🏠", label = "Home", isSelected = selectedTab == MainTab.HOME, onClick = { onTabSelected(MainTab.HOME) })
            NavItem(icon = "🎓", label = "Study", isSelected = selectedTab == MainTab.SUBJECTS, onClick = { onTabSelected(MainTab.SUBJECTS) })
            
            // Spacer to accommodate the central Quiz button
            Spacer(modifier = Modifier.width(60.dp))
            
            NavItem(icon = "🏆", label = "Ranks", isSelected = selectedTab == MainTab.LEADERBOARD, onClick = { onTabSelected(MainTab.LEADERBOARD) })
            NavItem(icon = "👤", label = "Profile", isSelected = selectedTab == MainTab.PROFILE, onClick = { onTabSelected(MainTab.PROFILE) })
        }
        
        // Custom central slightly larger circular gold-accented action button
        Box(
            modifier = Modifier
                .padding(bottom = 24.dp)
                .size(68.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFFFD700), Color(0xFFEAB308))
                    )
                )
                .border(2.dp, Color(0xFFFEF08A), CircleShape)
                .clickable { onTabSelected(MainTab.QUIZ) },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text(text = "🔥", fontSize = 22.sp)
                Text(text = "QUIZ", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.Black)
            }
        }
    }
}

@Composable
fun NavItem(icon: String, label: String, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        val alphaVal = if (isSelected) 1.0f else 0.4f
        val colorTint = if (isSelected) Color(0xFFEAB308) else TextPrimary
        
        Text(text = icon, fontSize = 22.sp, color = colorTint.copy(alpha = alphaVal))
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = colorTint.copy(alpha = alphaVal))
    }
}

@Composable
fun StudentQuizHubScreen(onNavigateToSubscription: () -> Unit) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
    ) {
        Box(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.TopEnd)
                .background(Brush.radialGradient(colors = listOf(Color(0x1F0EA5E9), Color.Transparent)))
                .blur(40.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("NECTA Exam", color = TextSecondary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text("Practice Arena", color = TextPrimary, fontSize = 32.sp, fontWeight = FontWeight.Black)
                Text("Simulate real national exams, daily challenges, and earn elite ranks.", color = TextSecondary, fontSize = 13.sp)
            }

            // Quick Streak Challenge Active Bannered Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0x33EAB308)),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, Color(0x80EAB308))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("🏆", fontSize = 40.sp)
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Weekly Champion Tournament", color = Color(0xFFEAB308), fontWeight = FontWeight.Black, fontSize = 14.sp)
                        Text("Compete in physics mock exam to win elite custom profile designs!", color = TextPrimary, fontSize = 12.sp)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFEAB308))
                            .clickable { /* Join tournament */ }
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Text("Join", fontWeight = FontWeight.Black, color = Color.Black, fontSize = 12.sp)
                    }
                }
            }

            // Practice Category options
            Text("Practice Exams By Form", color = TextPrimary, fontWeight = FontWeight.Black, fontSize = 18.sp)
            
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf(
                    "Form 4 Biology National Mock Challenge" to "20 Practice MCQs  •  Time: 30m",
                    "Form 3 Standard Geography Review" to "15 Multi-category questions",
                    "Form 2 Chemistry NECTA Preparation" to "10 True/False basics test",
                    "Form 1 Physics General Basics" to "10 Introductory MCQs"
                ).forEach { (quizTitle, descText) ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, Color(0xFF334155))
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFF0F172A)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("📝", fontSize = 20.sp)
                                }
                                Column {
                                    Text(quizTitle, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text(descText, color = TextSecondary, fontSize = 12.sp)
                                }
                            }
                            
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF334155))
                                    .clickable { /* Start quiz mock simulation */ }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text("Play", color = Color(0xFF38BDF8), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // Premium Features Lock Zone Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToSubscription() },
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, Color(0xFF334155))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("👑 AI Quiz Generator", color = Color(0xFFEAB308), fontWeight = FontWeight.Black, fontSize = 16.sp)
                        Text("Premium Zone 🔐", color = TextSecondary, fontSize = 11.sp)
                    }
                    Text(
                        "Generate personalized study packs, practice simulators, and error trackers with unlimited Premium benefits.",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(96.dp))
        }
    }
}
