package com.example.muslimone.data.repository

import com.example.muslimone.data.api.ApiService
import com.example.muslimone.data.model.SholatModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SholatRepository(private val apiService: ApiService) {

    suspend fun getPrayerTimes(city: String, country: String): Result<List<SholatModel>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getPrayerTimes(city, country)
                Result.success(mapResponseToModel(response))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getPrayerTimesByCoords(lat: Double, lon: Double): Result<List<SholatModel>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getPrayerTimesByCoords(lat, lon)
                Result.success(mapResponseToModel(response))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    private fun mapResponseToModel(response: com.example.muslimone.data.model.PrayerResponse): List<SholatModel> {
        val timings = response.data.timings
        return listOf(
            SholatModel("Imsak", timings["Imsak"] ?: ""),
            SholatModel("Subuh", timings["Fajr"] ?: ""),
            SholatModel("Dzuhur", timings["Dhuhr"] ?: ""),
            SholatModel("Ashar", timings["Asr"] ?: ""),
            SholatModel("Maghrib", timings["Maghrib"] ?: ""),
            SholatModel("Isya", timings["Isha"] ?: "")
        )
    }
}
