package com.example.ui.screens.main

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import com.example.ui.theme.*
import com.example.ui.components.AppCard
import com.example.ui.components.AppButton
import com.example.ui.components.XPProgressBar

@Composable
fun LeaderboardScreen(gamificationViewModel: com.example.ui.viewmodels.GamificationViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    var selectedTab by remember { mutableStateOf("National") }
    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 24.dp)) {
        Text("Top", fontSize = 24.sp, color = TextPrimary)
        Text("Scholars", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = PrimaryGreen)
        Spacer(modifier = Modifier.height(24.dp))
        
        Row(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(SurfaceDark).padding(4.dp)) {
            val tabs = listOf("National", "School", "Friends")
            tabs.forEach { tab ->
                val isSelected = selectedTab == tab
                Box(
                    modifier = Modifier.weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) PrimaryGreen.copy(alpha = 0.2f) else Color.Transparent)
                        .clickable { selectedTab = tab }
                        .padding(8.dp), 
                    contentAlignment = Alignment.Center
                ) {
                    Text(tab, color = if (isSelected) PrimaryGreen else TextSecondary, fontWeight = FontWeight.Bold)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        com.example.ui.components.PremiumLockCard(title = "Premium Tournaments", subtitle = "Compete for real prizes", onClick = {})
        Spacer(modifier = Modifier.height(16.dp))
        
        val mockData = List(10) { index -> 
            com.example.domain.models.LeaderboardUser(
                id = "u$index",
                name = if (index == 2) "Amani" else "Student ${index + 1}",
                xp = 5000 - index * 100,
                rank = index + 1
            )
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(mockData.size) { index ->
                val user = mockData[index]
                val isCurrentUser = user.name == "Amani"
                Row(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp))
                        .background(if (isCurrentUser) PrimaryGreen.copy(alpha = 0.1f) else SurfaceDark)
                        .border(1.dp, if (isCurrentUser) PrimaryGreen.copy(alpha = 0.4f) else BorderDark, RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text("#${user.rank}", color = if (user.rank <= 3) SecondaryOrange else TextSecondary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(0xFF1E293B)), contentAlignment = Alignment.Center) {
                            Text(user.avatar, fontSize = 20.sp)
                        }
                        Text(if (isCurrentUser) "You" else user.name, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                    }
                    Text("${user.xp} XP", color = PrimaryGreen, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun AnalyticsScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 24.dp)) {
        Text("Your", fontSize = 24.sp, color = TextPrimary)
        Text("Analytics", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = PrimaryGreen)
        Spacer(modifier = Modifier.height(24.dp))
        
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp), contentPadding = PaddingValues(bottom = 100.dp)) {
            item {
                AppCard {
                    Column {
                        Text("Mastery Level", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                        XPProgressBar(progress = 65, total = 100, label = "Overall Completion")
                    }
                }
            }
            
            item {
                AppCard {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text("Weekly Consistency", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                            val days = listOf("M", "T", "W", "T", "F", "S", "S")
                            val heights = listOf(40.dp, 80.dp, 60.dp, 100.dp, 30.dp, 50.dp, 0.dp)
                            
                            days.forEachIndexed { index, day ->
                                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Box(modifier = Modifier.width(24.dp).height(maxOf(4.dp, heights[index])).clip(CircleShape).background(if(heights[index] > 0.dp) PrimaryGreen else SurfaceVariantDark))
                                    Text(day, color = TextSecondary, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
            
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    AppCard(modifier = Modifier.weight(1f)) {
                        Column {
                            Text("Strengths", color = TextPrimary, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Biology", color = PrimaryGreen)
                            Text("Geography", color = PrimaryGreen)
                        }
                    }
                    AppCard(modifier = Modifier.weight(1f)) {
                        Column {
                            Text("Focus On", color = TextPrimary, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Mathematics", color = SecondaryOrange)
                            Text("Chemistry", color = SecondaryOrange)
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
                com.example.ui.components.PremiumLockCard(title = "Advanced Analytics", subtitle = "Unlock deep insights into your weak areas", onClick = {})
            }
            item {
                AppCard(backgroundColor = SurfaceDark.copy(alpha = 0.5f), borderColor = PrimaryGreen.copy(alpha = 0.3f)) {
                    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("NECTA Exam Readiness", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Keep studying to generate predictions.", color = TextSecondary, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(onNavigateToSettings: () -> Unit, onNavigateToSubscription: () -> Unit = {}, onNavigateToRewards: () -> Unit = {}, gamificationViewModel: com.example.ui.viewmodels.GamificationViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val scrollState = rememberScrollState()
    val gamificationState by gamificationViewModel.state.collectAsState()
    val profile = gamificationState.profile

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 24.dp).verticalScroll(scrollState), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            com.example.ui.screens.subscription.SubscriptionBadge(isPremium = false)
            IconButton(onClick = onNavigateToSettings) {
                Text("⚙️", fontSize = 24.sp)
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        Box(modifier = Modifier.size(96.dp).clip(CircleShape).background(SurfaceDark).border(2.dp, PrimaryGreen, CircleShape), contentAlignment = Alignment.Center) {
            Text("AM", fontSize = 36.sp, color = PrimaryGreen, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Amani", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text("Level ${profile.level} Scholar • Form 4", fontSize = 14.sp, color = TextSecondary)
        
        Spacer(modifier = Modifier.height(16.dp))
        AppButton(text = "Manage Subscription", onClick = onNavigateToSubscription, modifier = Modifier.fillMaxWidth(0.6f))
        
        // Placeholder for Family Plan
        Spacer(modifier = Modifier.height(12.dp))
        AppButton(
            text = "Family Profiles (1/4)",
            onClick = { /* TODO: Family Profiles Screen */ },
            modifier = Modifier.fillMaxWidth(0.6f),
            isSecondary = true
        )

        Spacer(modifier = Modifier.height(32.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Box(modifier = Modifier.clickable { onNavigateToRewards() }) {
                ProfileStat(profile.coins.toString(), "COINS")
            }
            ProfileStat(profile.currentStreak.toString(), "DAY STREAK")
            ProfileStat(profile.totalXP.toString(), "TOTAL XP")
        }

        
        Spacer(modifier = Modifier.height(32.dp))
        
        AppCard(modifier = Modifier.fillMaxWidth()) {
            Column {
                Text("Recent Badges", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    gamificationState.badges.filter { it.isUnlocked }.take(3).forEach { badge ->
                        AchievementBadge(badge.icon, badge.name)
                    }
                }
            }
        }
    }
}

@Composable
fun AchievementBadge(icon: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(80.dp)) {
        Box(modifier = Modifier.size(56.dp).clip(CircleShape).background(SurfaceVariantDark).border(1.dp, BorderDim, CircleShape), contentAlignment = Alignment.Center) {
            Text(icon, fontSize = 24.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(label, color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
    }
}

@Composable
fun ProfileStat(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = PrimaryGreen)
        Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextSecondary, letterSpacing = 1.sp)
    }
}
