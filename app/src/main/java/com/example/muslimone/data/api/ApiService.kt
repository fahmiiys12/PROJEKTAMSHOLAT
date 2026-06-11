package com.example.muslimone.data.api

import com.example.muslimone.data.model.PrayerResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("v1/timingsByCity")
    suspend fun getPrayerTimes(
        @Query("city") city: String,
        @Query("country") country: String,
        @Query("method") method: Int = 2
    ): PrayerResponse

    @GET("v1/timings")
    suspend fun getPrayerTimesByCoords(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("method") method: Int = 2
    ): PrayerResponse

    companion object {
        private const val BASE_URL = "https://api.aladhan.com/"

        fun create(): ApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}
