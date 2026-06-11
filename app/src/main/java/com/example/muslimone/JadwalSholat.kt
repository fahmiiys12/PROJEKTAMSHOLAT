package com.example.muslimone

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
    val imageSource: Any? = null,
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
            imageSource = R.drawable.jadwal_sholat,
            rute = "jadwal_sholat"
        ),
        FiturIbadah(
            "Al-Qur'an Digital", 
            "Baca dan pelajari kitab suci Al-Qur'an", 
            "Baca Sekarang", 
            Icons.AutoMirrored.Filled.MenuBook,
            0xFF00C853,
            imageSource = R.drawable.alquran_sholat,
            rute = "alquran"
        ),
        FiturIbadah(
            "Arah Kiblat", 
            "Cari arah kiblat dengan akurat", 
            "Buka Kompas", 
            Icons.Default.Explore,
            0xFF00C853,
            imageSource = R.drawable.kiblat_sholat,
            rute = "kiblat"
        ),
        FiturIbadah(
            "Kalender Sholat", 
            "Jadwal sholat lengkap sepanjang bulan", 
            "Lihat Kalender", 
            Icons.Default.CalendarMonth, 
            0xFF00C853,
            imageSource = R.drawable.kalender_sholat,
            rute = "kalender"
        ),
        FiturIbadah(
            "Doa & Dzikir", 
            "Kumpulan doa harian dan dzikir", 
            "Lihat Doa", 
            Icons.Default.Favorite, 
            0xFF00C853,
            imageSource = R.drawable.doa_sholat,
            rute = "doa"
        ),
        FiturIbadah(
            "Tasbih Digital", 
            "Hitung dzikir harian Anda", 
            "Mulai", 
            Icons.Default.AddCircle, 
            0xFF00C853,
            imageSource = R.drawable.tasbih_sholat,
            rute = "tasbih"
        ),
        FiturIbadah(
            "Asmaul Husna", 
            "99 nama Allah beserta maknanya", 
            "Pelajari", 
            Icons.Default.Star, 
            0xFF00C853,
            imageSource = R.drawable.asmaulhusna_sholat,
            rute = "asmaul_husna"
        ),
        FiturIbadah(
            "Masjid Terdekat", 
            "Temukan lokasi masjid di sekitar Anda", 
            "Cari Lokasi", 
            Icons.Default.LocationOn, 
            0xFF00C853,
            imageSource = R.drawable.masjid_sholat,
            rute = "masjid"
        ),
        FiturIbadah(
            "Zakat & Infaq", 
            "Salurkan bantuan Anda kepada yang membutuhkan", 
            "Donasi", 
            Icons.Default.VolunteerActivism, 
            0xFF00C853,
            imageSource = R.drawable.donasi_sholat,
            rute = "donasi"
        )
    )
}

