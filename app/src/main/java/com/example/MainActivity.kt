package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.*
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
import com.example.ui.theme.*
import com.example.ui.navigation.AppNavigation
import com.example.ui.screens.main.BottomNavBar
import com.example.ui.screens.main.MainTab        

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    onNavigateToSubscription: () -> Unit = {},
    onNavigateToMissions: () -> Unit = {},
    onNavigateToStudy: () -> Unit = {},
    gamificationViewModel: com.example.ui.viewmodels.GamificationViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val scrollState = rememberScrollState()
    val gamificationState by gamificationViewModel.state.collectAsState()
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
    ) {
        // Soft blue highlights and deep atmospheric glowing elements
        Box(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.TopStart)
                .offset(y = (-150).dp)
                .background(Brush.radialGradient(colors = listOf(Color(0x2238BDF8), Color.Transparent)))
                .blur(50.dp)
        )
        Box(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.CenterEnd)
                .offset(x = 100.dp)
                .background(Brush.radialGradient(colors = listOf(Color(0x11EAB308), Color.Transparent)))
                .blur(45.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // 1. Status/Top Bar with clean icons and custom initials
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1E293B))
                            .border(1.5.dp, Color(0xFFEAB308), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("AM", color = Color(0xFFEAB308), fontWeight = FontWeight.Black, fontSize = 16.sp)
                    }
                    Column {
                        Text(
                            text = "Hello, Amani! 👋",
                            color = TextPrimary,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "Keep learning, keep achieving!",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0x331E293B))
                            .clickable { /* Notification tap */ },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🔔", fontSize = 16.sp)
                    }
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0x331E293B))
                            .clickable { /* Menu tap */ },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("☰", fontSize = 16.sp, color = TextPrimary)
                    }
                }
            }

            // 2. Progress Summary Card (With Streak, Level, and Circular XP Ring)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color(0xFF334155))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text("🔥", fontSize = 18.sp)
                                    Text("7", color = Color(0xFFEAB308), fontWeight = FontWeight.Black, fontSize = 20.sp)
                                }
                                Text("Daily Streak", color = TextSecondary, fontSize = 11.sp)
                            }
                            
                            Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color(0xFF334155)))

                            Column {
                                Text("Lvl 12", color = TextPrimary, fontWeight = FontWeight.Black, fontSize = 20.sp)
                                Text("Level Index", color = TextSecondary, fontSize = 11.sp)
                            }
                        }

                        Column {
                            Text("XP: 2,450 Total", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Keep revision active to double reward points!", color = TextSecondary, fontSize = 11.sp)
                        }
                    }

                    // Circular progress ring mockup
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(88.dp)
                    ) {
                        CircularProgressIndicator(
                            progress = { 0.68f },
                            color = Color(0xFFEAB308),
                            trackColor = Color(0xFF0F172A),
                            strokeWidth = 8.dp,
                            modifier = Modifier.size(80.dp)
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "68%",
                                color = TextPrimary,
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp
                            )
                            Text(
                                "680/1k XP",
                                color = TextSecondary,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // 3. Recommended Revision Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0x33EAB308)),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.5.dp, Color(0x80EAB308))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            "Recommended Revision",
                            color = Color(0xFFEAB308),
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp,
                            letterSpacing = 1.sp
                        )
                        Text(
                            "Revise Algebra today",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                    Button(
                        onClick = onNavigateToStudy,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEAB308), contentColor = Color.Black),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text("Start", fontWeight = FontWeight.Black, fontSize = 13.sp)
                    }
                }
            }

            // 4. Continue Learning Section with course cards
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Continue Learning",
                        color = TextPrimary,
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp
                    )
                    Text(
                        "View all",
                        color = Color(0xFF38BDF8),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        modifier = Modifier.clickable { onNavigateToStudy() }
                    )
                }

                // Mathematics Course Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, Color(0xFF334155))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0x2238BDF8)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("📐", fontSize = 20.sp)
                                }
                                Column {
                                    Text("Mathematics", color = TextPrimary, fontWeight = FontWeight.Black, fontSize = 16.sp)
                                    Text("Linear Equations", color = TextSecondary, fontSize = 13.sp)
                                }
                            }

                            // Form 2 level indicator
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF334155))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text("Form 2", color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        // Progress linear gauge and play button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Progress", color = TextSecondary, fontSize = 11.sp)
                                    Text("68%", color = Color(0xFF38BDF8), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF0F172A))
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(0.68f)
                                            .fillMaxHeight()
                                            .clip(CircleShape)
                                            .background(Color(0xFF38BDF8))
                                    )
                                }
                            }

                            // Dynamic resume circle arrow
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEAB308))
                                    .clickable { onNavigateToStudy() },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("▶", color = Color.Black, fontSize = 13.sp, modifier = Modifier.offset(x = 1.dp))
                            }
                        }
                    }
                }
            }

            // 5. Today's Goals Section with 3 distinct custom mission rows
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Today’s Goals",
                        color = TextPrimary,
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp
                    )
                    Text(
                        "2 / 3 Completed",
                        color = Color(0xFFEAB308),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                // Mission columns
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Mission 1: Answer 10 MCQs (Completed)
                    MissionCardItem(
                        title = "Answer 10 MCQs",
                        scoreText = "10 / 10",
                        xpReward = "+100 XP",
                        isDone = true,
                        progress = 1.0f
                    )

                    // Mission 2: Study for 20 Minutes (In-progress)
                    MissionCardItem(
                        title = "Study for 20 Minutes",
                        scoreText = "12 / 20",
                        xpReward = "+100 XP",
                        isDone = false,
                        progress = 0.6f
                    )

                    // Mission 3: Get 5 Correct in a Row (In-progress)
                    MissionCardItem(
                        title = "Get 5 Correct in a Row",
                        scoreText = "3 / 5",
                        xpReward = "+150 XP",
                        isDone = false,
                        progress = 0.6f
                    )
                }
            }

            // 6. Premium Zone Cards
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Column {
                    Text(
                        "Premium Zone",
                        color = TextPrimary,
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp
                    )
                    Text(
                        "Unlock powerful learning features",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }

                // Horizontally Scrollable or neatly stacked locked features
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    listOf(
                        "AI Explanations" to "📝",
                        "Higher Order" to "🧠",
                        "Simulations" to "⏱️"
                    ).forEach { (featName, emojiIcon) ->
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onNavigateToSubscription() },
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, Color(0xFF334155))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 14.dp, horizontal = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(emojiIcon, fontSize = 16.sp)
                                    Text("🔐", fontSize = 11.sp)
                                }
                                Text(
                                    text = featName,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1
                                )
                                Text(
                                    text = "Premium",
                                    color = Color(0xFFEAB308),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(88.dp)) // Cushioning block for smooth scrolling above Nav Bar
        }
    }
}

@Composable
fun MissionCardItem(
    title: String,
    scoreText: String,
    xpReward: String,
    isDone: Boolean,
    progress: Float
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, if (isDone) Color(0xFF10B981).copy(alpha = 0.3f) else Color(0xFF334155))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Checked state icon
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(if (isDone) Color(0xFF10B981) else Color(0xFF0F172A))
                    .border(1.dp, if (isDone) Color.Transparent else Color(0xFF475569), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (isDone) {
                    Text("✓", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }

            // Description column
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = title,
                        color = if (isDone) TextSecondary else TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = if (isDone) FontWeight.Medium else FontWeight.Bold
                    )
                    Text(
                        text = scoreText,
                        color = if (isDone) Color(0xFF10B981) else TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                // Subtle inline progress line
                if (!isDone) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF0F172A))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(progress)
                                .fillMaxHeight()
                                .clip(CircleShape)
                                .background(Color(0xFFEAB308))
                        )
                    }
                }
            }

            // XP badge award
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isDone) Color(0x1110B981) else Color(0x33EAB308))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = xpReward,
                    color = if (isDone) Color(0xFF10B981) else Color(0xFFEAB308),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}
