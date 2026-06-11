package com.example.muslimone.data.repository

import com.example.muslimone.data.api.AuthApiService
import com.example.muslimone.data.model.User

class AuthRepository(private val apiService: AuthApiService) {
    suspend fun getUsers(): List<User> {
        return apiService.getUsers()
    }
}
