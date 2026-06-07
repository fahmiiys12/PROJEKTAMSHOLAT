package com.example.projektamsholat.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projektamsholat.data.model.SholatModel
import com.example.projektamsholat.data.repository.SholatRepository
import kotlinx.coroutines.launch

class SholatViewModel(private val repository: SholatRepository) : ViewModel() {

    var sholatList by mutableStateOf<List<SholatModel>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var isError by mutableStateOf(false)
        private set

    fun loadData(city: String = "Bandar Lampung", country: String = "Indonesia") {
        viewModelScope.launch {
            isLoading = true
            isError = false
            
            val result = repository.getPrayerTimes(city, country)
            
            result.onSuccess {
                sholatList = it
                isLoading = false
            }.onFailure {
                isError = true
                isLoading = false
            }
        }
    }
}
