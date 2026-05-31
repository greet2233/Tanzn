package com.example.ui.screens.subscription

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
import com.example.domain.models.SubscriptionMockData
import com.example.domain.models.SubscriptionPlan
import com.example.ui.components.AppButton
import com.example.ui.theme.*

@Composable
fun PremiumUpgradeModal(
    onDismiss: () -> Unit,
    onNavigateToSubscription: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f))
            .clickable(onClick = onDismiss),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(24.dp))
                .background(SurfaceDark)
                .clickable { /* prevent child clicks from dismissing */ }
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEAB308).copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text("⭐", fontSize = 32.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Premium Feature", color = Color(0xFFEAB308), fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Unlock full access to summaries, higher order questions, exam mode, and unlimited quizzes.",
                color = TextSecondary,
                fontSize = 14.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            AppButton(
                text = "See Premium Plans",
                onClick = onNavigateToSubscription,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            TextButton(onClick = onDismiss) {
                Text("Maybe Later", color = TextSecondary)
            }
        }
    }
}

@Composable
fun SubscriptionScreen(
    onNavigateBack: () -> Unit,
    onNavigateToPayment: (String) -> Unit
) {
    val plans = SubscriptionMockData.plans

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 40.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(SurfaceDark)
                    .clickable(onClick = onNavigateBack),
                contentAlignment = Alignment.Center
            ) {
                Text("←", color = TextPrimary, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text("Go Premium", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(plans.size) { index ->
                val plan = plans[index]
                SubscriptionPlanCard(plan = plan, onClick = { onNavigateToPayment(plan.id) })
            }
            
            item {
                Spacer(modifier = Modifier.height(24.dp))
                AppButton(
                    text = "WhatsApp Support (Pay Offline)",
                    onClick = { /* Open WhatsApp */ },
                    modifier = Modifier.fillMaxWidth(),
                    isSecondary = true
                )
            }
        }
    }
}

@Composable
fun SubscriptionPlanCard(plan: SubscriptionPlan, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(SurfaceDark)
            .border(1.dp, Color(0xFFEAB308).copy(alpha = 0.5f), RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(plan.name, color = Color(0xFFEAB308), fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("${plan.priceTsh} TSH /mo", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        plan.features.forEach { feature ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Text("✔", color = PrimaryGreen, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(feature, color = TextSecondary, fontSize = 14.sp)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        AppButton(
            text = "Select Plan",
            onClick = onClick,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun PaymentMethodScreen(
    planId: String,
    onNavigateBack: () -> Unit
) {
    val methods = listOf("Google Play Billing", "Airtel Money", "M-Pesa", "Tigo Pesa", "Halopesa")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 40.dp, start = 20.dp, end = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(SurfaceDark)
                    .clickable(onClick = onNavigateBack),
                contentAlignment = Alignment.Center
            ) {
                Text("←", color = TextPrimary, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text("Select Payment", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        }

        Spacer(modifier = Modifier.height(32.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(methods.size) { index ->
                val method = methods[index]
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceDark)
                        .clickable { /* Create Payment Request */ }
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(method, color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    Text("▶", color = PrimaryGreen, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun UsageRemainingCard() {
    val usage = SubscriptionMockData.dailyUsage
    val limits = SubscriptionMockData.limits
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceDark)
            .border(1.dp, BorderDark, RoundedCornerShape(16.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        UsageStat("MCQ", usage.mcqUsed, limits["mcq"] ?: 5)
        UsageStat("FIB", usage.fibUsed, limits["fib"] ?: 3)
        UsageStat("T/F", usage.tfUsed, limits["tf"] ?: 2)
    }
}

@Composable
fun UsageStat(label: String, used: Int, limit: Int) {
    val remaining = limit - used
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = TextSecondary, fontSize = 12.sp)
        Text("$remaining left", color = if (remaining > 0) PrimaryGreen else SecondaryOrange, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun SubscriptionBadge(isPremium: Boolean) {
    if (isPremium) {
        Box(
            modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(Color(0xFFEAB308).copy(alpha = 0.2f)).padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text("⭐ PREMIUM", color = Color(0xFFEAB308), fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    } else {
        Box(
            modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(SurfaceDark).padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text("FREE PLAN", color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}
