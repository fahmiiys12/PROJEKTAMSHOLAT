package com.example.projektamsholat.data.repository

import com.example.projektamsholat.data.api.ApiService
import com.example.projektamsholat.data.model.SholatModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SholatRepository(private val apiService: ApiService) {

    suspend fun getPrayerTimes(city: String, country: String): Result<List<SholatModel>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getPrayerTimes(city, country)
                val timings = response.data.timings
                val list = listOf(
                    SholatModel("Imsak", timings["Imsak"] ?: ""),
                    SholatModel("Subuh", timings["Fajr"] ?: ""),
                    SholatModel("Dzuhur", timings["Dhuhr"] ?: ""),
                    SholatModel("Ashar", timings["Asr"] ?: ""),
                    SholatModel("Maghrib", timings["Maghrib"] ?: ""),
                    SholatModel("Isya", timings["Isha"] ?: "")
                )
                Result.success(list)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
