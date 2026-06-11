package com.example.muslimone.data.api

import com.example.muslimone.data.model.User
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface AuthApiService {
    @GET("login.json")
    suspend fun getUsers(): List<User>

    companion object {
        private const val BASE_URL = "https://raw.githubusercontent.com/fahmiiys12/loginjson/refs/heads/main/"

        fun create(): AuthApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AuthApiService::class.java)
        }
    }
}
