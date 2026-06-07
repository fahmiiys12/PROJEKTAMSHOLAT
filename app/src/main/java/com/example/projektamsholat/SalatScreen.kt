package com.example.projektamsholat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

/**
 * Data class untuk merepresentasikan item jadwal sholat sesuai standar Modul 6.
 */
data class SalatItem(
    val name: String,
    val time: String,
    val icon: ImageVector,
    var isAlarmOn: Boolean,
    val isActive: Boolean = false,
    val hasSound: Boolean = true
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalatScreen(navController: NavController) {
    // Data dummy sesuai referensi gambar menu.jpeg
    val daftarSalat = listOf(
        SalatItem("Fajr", "04:42", Icons.Default.Bedtime, true),
        SalatItem("Matahari terbit", "06:00", Icons.Default.WbSunny, false, hasSound = false),
        SalatItem("Dhuhr", "12:01", Icons.Default.WbSunny, true),
        SalatItem("Asr", "15:23", Icons.Default.LightMode, true, isActive = true),
        SalatItem("Maghrib", "17:56", Icons.Default.WbTwilight, true),
        SalatItem("Isha", "19:09", Icons.Default.NightsStay, true)
    )

    Scaffold(
        containerColor = Color(0xFF121212) // Menggunakan Dark Theme sesuai spesifikasi
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // 1. Header Visual (Bagian Atas Hijau)
            SalatVisualHeader()

            // 2. Container Jadwal Sholat (Card Gelap)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 16.dp),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Header Tanggal Hijriah
                    DateHeaderRow()

                    Spacer(modifier = Modifier.height(8.dp))

                    // 3. Lazy Layout untuk efisiensi render (Modul 6)
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(daftarSalat) { salat ->
                            SalatItemRow(salat)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SalatVisualHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF00C853), Color(0xFF00A544))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Asr",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White.copy(alpha = 0.9f)
            )
            Text(
                text = "15:23",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 72.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White
            )
            Text(
                text = "Sholat berikutnya 02 : 59 : 04",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun DateHeaderRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { }) {
            Icon(Icons.Default.ChevronLeft, contentDescription = null, tint = Color(0xFF00C853))
        }
        Text(
            text = "Dhul-Hijjah 21, 1447 AH",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        IconButton(onClick = { }) {
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFF00C853))
        }
    }
}

/**
 * Composable function terpisah untuk render tiap baris (Modul 7)
 */
@Composable
fun SalatItemRow(salat: SalatItem) {
    var isAlarmOn by remember { mutableStateOf(salat.isAlarmOn) }

    // Penentuan warna berdasarkan state aktif
    val rowBackground = if (salat.isActive) Color(0xFF00C853).copy(alpha = 0.15f) else Color.Transparent
    val textColor = if (salat.isActive) Color(0xFF00C853) else Color.White
    val iconTint = if (salat.isActive) Color(0xFF00C853) else Color(0xFF00C853).copy(alpha = 0.7f)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        color = rowBackground
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Bagian Kiri: Ikon & Nama
            Icon(
                imageVector = salat.icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(
                text = salat.name,
                style = MaterialTheme.typography.bodyLarge,
                color = textColor,
                fontWeight = if (salat.isActive) FontWeight.Bold else FontWeight.Normal,
                modifier = Modifier.weight(1f)
            )

            // Indikator Suara/Alarm (Speaker Icon)
            if (salat.hasSound) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                    contentDescription = null,
                    tint = if (isAlarmOn) Color(0xFF00C853) else Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(24.dp))

            // Bagian Tengah: Waktu
            Text(
                text = salat.time,
                style = MaterialTheme.typography.bodyLarge,
                color = textColor,
                fontWeight = if (salat.isActive) FontWeight.Bold else FontWeight.Normal
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Bagian Kanan: Switch Toggle (Material 3)
            Switch(
                checked = isAlarmOn,
                onCheckedChange = { isAlarmOn = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFF00C853),
                    uncheckedThumbColor = Color.Gray,
                    uncheckedTrackColor = Color.DarkGray
                )
            )
        }
    }
}
