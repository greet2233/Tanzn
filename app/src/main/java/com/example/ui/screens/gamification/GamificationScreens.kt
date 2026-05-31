package com.example.ui.screens.gamification

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
import com.example.ui.components.AppButton
import com.example.ui.theme.*
import com.example.ui.viewmodels.GamificationViewModel

@Composable
fun DailyMissionsScreen(
    viewModel: GamificationViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    
    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(
                modifier = Modifier.size(40.dp).clip(CircleShape).background(SurfaceDark).clickable { onNavigateBack() },
                contentAlignment = Alignment.Center
            ) {
                Text("←", color = TextPrimary, fontSize = 20.sp)
            }
            Text("Daily Missions", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(state.missions.size) { index ->
                val mission = state.missions[index]
                MissionCard(mission = mission, onClaim = { viewModel.claimMission(mission.id) })
            }
        }
    }
}

@Composable
fun MissionCard(mission: com.example.domain.models.DailyMission, onClaim: () -> Unit) {
    val progress = if (mission.targetValue > 0) mission.currentValue.toFloat() / mission.targetValue else 0f
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(SurfaceDark)
            .border(1.dp, BorderDark, RoundedCornerShape(20.dp))
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(mission.title, color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Text(mission.description, color = TextSecondary, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(12.dp))
            
            // Progress Bar
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier.weight(1f).height(8.dp).clip(CircleShape).background(SurfaceVariantDark)
                ) {
                    Box(modifier = Modifier.fillMaxHeight().fillMaxWidth(progress).clip(CircleShape).background(PrimaryGreen))
                }
                Text("${mission.currentValue}/${mission.targetValue}", color = TextSecondary, fontSize = 12.sp)
            }
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        if (mission.isClaimed) {
            Box(modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(SurfaceVariantDark).padding(horizontal = 16.dp, vertical = 8.dp)) {
                Text("CLAIMED", color = TextSecondary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        } else if (mission.isCompleted) {
            Box(
                modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(PrimaryGreen).clickable(onClick = onClaim).padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text("CLAIM", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        } else {
            Column(horizontalAlignment = Alignment.End) {
                Text("+${mission.xpReward} XP", color = PrimaryGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("+${mission.coinReward} 💎", color = Color(0xFFFFB800), fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun RewardsScreen(
    viewModel: GamificationViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val coins = state.profile.coins
    
    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(40.dp).clip(CircleShape).background(SurfaceDark).clickable { onNavigateBack() },
                contentAlignment = Alignment.Center
            ) {
                Text("←", color = TextPrimary, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text("Rewards Shop", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFFFB800).copy(alpha = 0.1f))
                .border(2.dp, Color(0xFFFFB800), RoundedCornerShape(24.dp))
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("💎", fontSize = 48.sp)
                Text("$coins", fontSize = 36.sp, fontWeight = FontWeight.Black, color = Color(0xFFFFB800))
                Text("COINS BALANCE", fontSize = 12.sp, color = Color(0xFFFFB800), fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            }
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Text("Coming Soon", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Use your coins to buy new avatars, themes, and enter premium tournaments.", color = TextSecondary, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            RewardBoxPlaceholder("Avatar Box", "100 💎", "👤")
            RewardBoxPlaceholder("Theme Box", "250 💎", "🎨")
        }
    }
}

@Composable
fun RowScope.RewardBoxPlaceholder(title: String, price: String, icon: String) {
    Column(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(20.dp))
            .background(SurfaceDark)
            .border(1.dp, BorderDark, RoundedCornerShape(20.dp))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.size(64.dp).clip(RoundedCornerShape(16.dp)).background(Color(0xFF1E293B)), contentAlignment = Alignment.Center) {
            Text(icon, fontSize = 32.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(title, color = TextPrimary, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        AppButton(text = price, onClick = {}, modifier = Modifier.fillMaxWidth(), isSecondary = true)
    }
}
