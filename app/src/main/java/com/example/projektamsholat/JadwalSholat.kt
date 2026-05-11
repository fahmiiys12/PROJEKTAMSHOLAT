package com.example.projektamsholat

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

data class JadwalSholat(
    val nama: String,
    val waktu: String,
    val status: String,
    val ikon: ImageVector
)

object SholatSource {
    val daftarSholat = listOf(
        JadwalSholat("Subuh", "04:30", "Sudah", Icons.Default.WbTwilight),
        JadwalSholat("Dzuhur", "12:00", "Belum", Icons.Default.WbSunny),
        JadwalSholat("Ashar", "15:15", "Belum", Icons.Default.WbCloudy),
        JadwalSholat("Maghrib", "18:05", "Belum", Icons.Default.NightsStay),
        JadwalSholat("Isya", "19:15", "Belum", Icons.Default.Bedtime)
    )
}
