package com.example.muslimone

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
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
import androidx.navigation.NavController
import com.example.muslimone.ui.DetailHeader

@Composable
fun TasbihScreen(navController: NavController) {
    var count by remember { mutableIntStateOf(0) }
    var target by remember { mutableIntStateOf(33) }
    val progress = count.toFloat() / target

    Scaffold(
        containerColor = Color(0xFF121212)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DetailHeader(
                title = "Tasbih Digital",
                subtitle = "Berdzikir mengingat Allah",
                imageSource = R.drawable.tasbih_sholat,
                onBack = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .size(250.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.05f))
                    .clickable { 
                        if (count < target) count++ else count = 1
                    },
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.size(230.dp),
                    color = Color(0xFF00C853),
                    strokeWidth = 8.dp,
                    trackColor = Color.White.copy(alpha = 0.1f),
                )
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = count.toString(),
                        style = MaterialTheme.typography.displayLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "/ $target",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                listOf(33, 99, 100).forEach { valTarget ->
                    FilterChip(
                        selected = target == valTarget,
                        onClick = { target = valTarget; count = 0 },
                        label = { Text(valTarget.toString()) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF00C853),
                            selectedLabelColor = Color.Black,
                            containerColor = Color.White.copy(alpha = 0.1f),
                            labelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            IconButton(
                onClick = { count = 0 },
                modifier = Modifier
                    .size(64.dp)
                    .background(Color.White.copy(alpha = 0.1f), CircleShape)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Reset", tint = Color.White)
            }

            Spacer(modifier = Modifier.weight(1f))
            
            Text(
                text = "Tap lingkaran untuk menghitung",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 32.dp)
            )
        }
    }
}
