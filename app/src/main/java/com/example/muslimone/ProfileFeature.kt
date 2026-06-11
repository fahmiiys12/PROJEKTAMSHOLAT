package com.example.muslimone

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ProfileScreen(
    navController: NavController,
    isDarkTheme: Boolean,
    onThemeToggle: (Boolean) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                ProfileHeaderSection()
            }

            item {
                DonationCard()
            }

            item {
                ProfileQuickAccessGrid(navController)
            }

            item {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    ThemeToggleItem(isDarkTheme, onThemeToggle)

                    ProfileMenuItem(Icons.Default.Notifications, "Jadwal Sholat") {
                        navController.navigate("jadwal_sholat")
                    }
                    ProfileMenuItem(Icons.Default.CalendarMonth, "Kalender Hijriah") {
                        navController.navigate("kalender")
                    }
                    ProfileMenuItem(Icons.Default.LocationOn, "Masjid Terdekat") {
                        navController.navigate("masjid")
                    }
                    ProfileMenuItem(Icons.Default.Star, "Asmaul Husna") {
                        navController.navigate("asmaul_husna")
                    }
                    ProfileMenuItem(Icons.Default.CardGiftcard, "Kotak Donasi") {
                    }
                    ProfileMenuItem(Icons.Default.Settings, "Pengaturan Adzan") {
                    }
                    ProfileMenuItem(Icons.Default.QuestionAnswer, "Bantuan & Feedback") {
                    }
                }
            }
            
            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
fun ProfileHeaderSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(60.dp),
            shape = CircleShape,
            color = Color(0xFF00C853)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text("F", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column {
            Text(
                text = "Assalamualikum, Fahmi",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "MuslimOne User",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun DonationCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color(0xFF004D40), Color(0xFF00C853))
                    )
                )
                .padding(16.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Favorite, null, tint = Color.White, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Dukung Dakwah", color = Color.White, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Mari berdonasi untuk pengembangan fitur ibadah yang lebih baik.",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 11.sp,
                    lineHeight = 16.sp,
                    modifier = Modifier.fillMaxWidth(0.7f)
                )
            }
            
            Button(
                onClick = { },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .height(36.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f)),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
            ) {
                Text("Donasi >", color = Color.White, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun ProfileQuickAccessGrid(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        QuickAccessItem(Icons.AutoMirrored.Filled.MenuBook, "Al-Qur'an", Color(0xFF4CAF50)) {
            navController.navigate("alquran")
        }
        QuickAccessItem(Icons.Default.Explore, "Kiblat", Color(0xFF2196F3)) {
            navController.navigate("kiblat")
        }
        QuickAccessItem(Icons.Default.Favorite, "Doa", Color(0xFFFF5252)) {
            navController.navigate("doa")
        }
        QuickAccessItem(Icons.Default.AddCircle, "Tasbih", Color(0xFFFF9800)) {
            navController.navigate("tasbih")
        }
    }
}

@Composable
fun QuickAccessItem(icon: ImageVector, label: String, color: Color, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Surface(
            modifier = Modifier.size(50.dp),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = color, modifier = Modifier.size(28.dp))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
    }
}

@Composable
fun ThemeToggleItem(isDarkTheme: Boolean, onThemeToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onThemeToggle(!isDarkTheme) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = if (isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Tema " + if (isDarkTheme) "Gelap" else "Terang",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp
            )
        }
        Switch(
            checked = isDarkTheme,
            onCheckedChange = { onThemeToggle(it) },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFF00C853),
                checkedTrackColor = Color(0xFF00C853).copy(alpha = 0.5f)
            )
        )
    }
}

@Composable
fun ProfileMenuItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp, modifier = Modifier.weight(1f))
        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
    }
}
