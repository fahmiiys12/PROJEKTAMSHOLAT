package com.example.muslimone.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muslimone.data.model.SholatModel
import com.example.muslimone.data.repository.SholatRepository
import kotlinx.coroutines.launch

import android.content.Context
import com.example.muslimone.utils.NetworkUtils

class SholatViewModel(private val repository: SholatRepository) : ViewModel() {

    var sholatList by mutableStateOf<List<SholatModel>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var isError by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun loadData(context: Context, city: String = "Bandar Lampung", country: String = "Indonesia") {
        if (!NetworkUtils.isInternetAvailable(context)) {
            isError = true
            errorMessage = "Tidak ada koneksi internet"
            return
        }
        
        viewModelScope.launch {
            isLoading = true
            isError = false
            val result = repository.getPrayerTimes(city, country)
            handleResult(result)
        }
    }

    fun loadDataByCoords(context: Context, lat: Double, lon: Double) {
        if (!NetworkUtils.isInternetAvailable(context)) {
            isError = true
            errorMessage = "Tidak ada koneksi internet"
            return
        }

        viewModelScope.launch {
            isLoading = true
            isError = false
            val result = repository.getPrayerTimesByCoords(lat, lon)
            handleResult(result)
        }
    }

    private fun handleResult(result: Result<List<SholatModel>>) {
        result.onSuccess {
            sholatList = it
            isLoading = false
            errorMessage = null
        }.onFailure {
            isError = true
            isLoading = false
            errorMessage = "Gagal memuat data: ${it.message}"
        }
    }
}
