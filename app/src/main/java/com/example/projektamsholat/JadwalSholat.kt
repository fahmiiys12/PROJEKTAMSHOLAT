package com.example.projektamsholat

data class JadwalSholat(
    val nama: String,
    val waktu: String,
    val status: String
)

object SholatSource {
    val daftarSholat = listOf(
        JadwalSholat("Subuh", "04:30", "Sudah"),
        JadwalSholat("Dzuhur", "12:00", "Belum"),
        JadwalSholat("Ashar", "15:15", "Belum"),
        JadwalSholat("Maghrib", "18:05", "Belum"),
        JadwalSholat("Isya", "19:15", "Belum")
    )
}
