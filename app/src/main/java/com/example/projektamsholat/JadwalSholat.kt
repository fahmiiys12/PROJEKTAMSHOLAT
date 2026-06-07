package com.example.projektamsholat

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*

data class FiturIbadah(
    val nama: String,
    val deskripsi: String,
    val labelTombol: String,
    val ikon: ImageVector,
    val warnaDasar: Long,
    val imageUrl: String? = null,
    val rute: String
)

object SholatSource {
    val daftarFitur = listOf(
        FiturIbadah(
            "Jadwal Sholat", 
            "Waktu Sholat berikutnya: Maghrib (18:05)", 
            "Atur Alarm", 
            Icons.Default.Notifications, 
            0xFF00C853,
            imageUrl = "https://images.unsplash.com/photo-1591604129939-f1efa4d9f7fa?q=80&w=500&auto=format&fit=crop",
            rute = "jadwal_sholat"
        ),
        FiturIbadah(
            "Al-Qur'an Digital", 
            "Baca dan pelajari kitab suci Al-Qur'an", 
            "Baca Sekarang", 
            Icons.AutoMirrored.Filled.MenuBook,
            0xFF00C853,
            imageUrl = "https://images.unsplash.com/photo-1609599006353-e629aaabfeae?q=80&w=500&auto=format&fit=crop",
            rute = "alquran"
        ),
        FiturIbadah(
            "Arah Kiblat", 
            "Cari arah kiblat dengan akurat", 
            "Buka Kompas", 
            Icons.Default.Explore,
            0xFF00C853,
            imageUrl = "https://images.unsplash.com/photo-1542810634-71277d95dcbb?q=80&w=500&auto=format&fit=crop",
            rute = "kiblat"
        ),
        FiturIbadah(
            "Kalender Sholat", 
            "Jadwal sholat lengkap sepanjang bulan", 
            "Lihat Kalender", 
            Icons.Default.CalendarMonth, 
            0xFF00C853,
            imageUrl = "https://images.unsplash.com/photo-1506784919141-935049915272?q=80&w=500&auto=format&fit=crop",
            rute = "kalender"
        ),
        FiturIbadah(
            "Doa & Dzikir", 
            "Kumpulan doa harian dan dzikir", 
            "Lihat Doa", 
            Icons.Default.Favorite, 
            0xFF00C853,
            imageUrl = "https://images.unsplash.com/photo-1584551271441-705307579684?q=80&w=500&auto=format&fit=crop",
            rute = "doa"
        ),
        FiturIbadah(
            "Tasbih Digital", 
            "Hitung dzikir harian Anda", 
            "Mulai", 
            Icons.Default.AddCircle, 
            0xFF00C853,
            imageUrl = "https://images.unsplash.com/photo-1564121211835-e88c852648ab?q=80&w=500&auto=format&fit=crop",
            rute = "tasbih"
        ),
        FiturIbadah(
            "Asmaul Husna", 
            "99 nama Allah beserta maknanya", 
            "Pelajari", 
            Icons.Default.Star, 
            0xFF00C853,
            imageUrl = "https://images.unsplash.com/photo-1519817650390-64a93db51149?q=80&w=500&auto=format&fit=crop",
            rute = "asmaul_husna"
        ),
        FiturIbadah(
            "Masjid Terdekat", 
            "Temukan lokasi masjid di sekitar Anda", 
            "Cari Lokasi", 
            Icons.Default.LocationOn, 
            0xFF00C853,
            imageUrl = "https://images.unsplash.com/photo-1542623024-a797a7a48c60?q=80&w=500&auto=format&fit=crop",
            rute = "masjid"
        )
    )
}
