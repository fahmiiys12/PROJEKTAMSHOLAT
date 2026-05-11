package com.example.projektamsholat

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

data class FiturIbadah(
    val nama: String,
    val deskripsi: String,
    val labelTombol: String,
    val ikon: ImageVector,
    val warnaDasar: Long
)

object SholatSource {
    val daftarFitur = listOf(
        FiturIbadah(
            "Pengingat Sholat", 
            "Waktu Sholat berikutnya: Maghrib (18:05)", 
            "Atur Alarm", 
            Icons.Default.Notifications, 
            0xFF4CAF50
        ),
        FiturIbadah(
            "Al-Qur'an Digital", 
            "Terakhir dibaca: Al-Baqarah ayat 15", 
            "Baca Sekarang", 
            Icons.Default.List, 
            0xFF2196F3
        ),
        FiturIbadah(
            "Arah Kiblat", 
            "Cari arah kiblat dengan akurat", 
            "Buka Kompas", 
            Icons.Default.Search,
            0xFF795548
        ),
        FiturIbadah(
            "Doa & Dzikir", 
            "Kumpulan doa harian lengkap", 
            "Lihat Doa", 
            Icons.Default.Favorite, 
            0xFFE91E63
        ),
        FiturIbadah(
            "Masjid Terdekat", 
            "Temukan lokasi masjid di sekitar Anda", 
            "Cari Lokasi", 
            Icons.Default.LocationOn, 
            0xFF607D8B
        )
    )
}
