package com.example.ui.components

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isSecondary: Boolean = false
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSecondary) SurfaceDark else PrimaryGreen,
            contentColor = if (isSecondary) TextPrimary else Color.Black,
            disabledContainerColor = SurfaceDark.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(text, fontWeight = FontWeight.Black, fontSize = 16.sp)
    }
}

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = SurfaceDark,
    borderColor: Color = BorderDark,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    var boxModifier = modifier
        .clip(RoundedCornerShape(24.dp))
        .background(backgroundColor)
        .border(1.dp, borderColor, RoundedCornerShape(24.dp))
        
    onClick?.let { 
        boxModifier = boxModifier.clickable(onClick = it)
    }
    
    Box(
        modifier = boxModifier.padding(20.dp),
        content = content
    )
}

@Composable
fun PremiumLockCard(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    AppCard(
        backgroundColor = SurfaceDark.copy(alpha = 0.5f),
        borderColor = SecondaryOrange.copy(alpha = 0.3f),
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(title, color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(subtitle, color = SecondaryOrange, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(SecondaryOrange.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text("🔒", fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun XPProgressBar(progress: Int, total: Int, label: String = "XP Progress") {
    val progressFloat = if (total > 0) progress.toFloat() / total.toFloat() else 0f
    
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text("$progress / $total", color = PrimaryGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.1f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progressFloat)
                    .fillMaxHeight()
                    .clip(CircleShape)
                    .background(PrimaryGreen)
            )
        }
    }
}
