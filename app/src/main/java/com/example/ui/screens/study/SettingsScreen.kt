package com.example.ui.screens.study

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun SettingsScreen(onNavigateBack: () -> Unit, onNavigateToAdmin: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(20.dp)) {
        Spacer(modifier = Modifier.height(24.dp))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(
                modifier = Modifier.size(40.dp).clip(CircleShape).background(SurfaceDark).clickable(onClick = onNavigateBack),
                contentAlignment = Alignment.Center
            ) {
                Text("←", color = TextPrimary, fontSize = 20.sp)
            }
            Text("Settings", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text("General", color = PrimaryGreen, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        SettingRow("Dark Mode", "On")
        SettingRow("Notifications", "Enabled")
        SettingRow("Language", "English")
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text("Account", color = SecondaryOrange, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        SettingRow("Subscription", "Free")
        SettingRow("Logout", "")

        Spacer(modifier = Modifier.height(32.dp))
        
        Text("Administration", color = PrimaryGreen, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToAdmin() }
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("AI Quiz Generator (Admin Portal)", color = TextPrimary, fontSize = 16.sp)
            Text("→", color = PrimaryGreen, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SettingRow(title: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, color = TextPrimary, fontSize = 16.sp)
        if (value.isNotEmpty()) {
            Text(value, color = TextSecondary, fontSize = 14.sp)
        }
    }
}
