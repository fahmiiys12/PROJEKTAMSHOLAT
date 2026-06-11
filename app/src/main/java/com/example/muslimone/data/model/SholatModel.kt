package com.example.muslimone.data.model

data class SholatModel(
    val name: String,
    val time: String
)

data class PrayerResponse(
    val data: PrayerData
)

data class PrayerData(
    val timings: Map<String, String>
)
