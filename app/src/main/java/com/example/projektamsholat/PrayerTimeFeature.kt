package com.example.projektamsholat

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.text.SimpleDateFormat
import java.util.*

// API Models
data class ApiResponse(val status: Boolean, val data: PrayerData)
data class PrayerData(val jadwal: Jadwal)
data class Jadwal(
    val tanggal: String,
    val imsak: String,
    val subuh: String,
    val terbit: String,
    val dhuha: String,
    val dzuhur: String,
    val ashar: String,
    val maghrib: String,
    val isya: String
)

interface SholatApiService {
    @GET("sholat/jadwal/1301/{tahun}/{bulan}/{tanggal}")
    suspend fun getJadwal(
        @Path("tahun") tahun: String,
        @Path("bulan") bulan: String,
        @Path("tanggal") tanggal: String
    ): ApiResponse
}

object RetrofitClient {
    private const val BASE_URL = "https://api.myquran.com/v2/"
    val service: SholatApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SholatApiService::class.java)
    }
}

data class PrayerUiState(
    val prayerData: Jadwal? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class PrayerViewModel : ViewModel() {
    var uiState by mutableStateOf(PrayerUiState())
        private set
    
    var currentLocationName by mutableStateOf("Memuat lokasi...")
        private set

    init {
        fetchData()
    }

    fun updateLocation(context: Context) {
        viewModelScope.launch {
            currentLocationName = LocationHelper.getCurrentLocationName(context)
        }
    }

    fun fetchData() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            try {
                val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                val currentDate = sdf.format(Date()).split("/")
                val response = RetrofitClient.service.getJadwal(currentDate[0], currentDate[1], currentDate[2])
                if (response.status) {
                    uiState = uiState.copy(prayerData = response.data.jadwal, isLoading = false)
                } else {
                    uiState = uiState.copy(errorMessage = "Gagal mengambil data dari server.", isLoading = false)
                }
            } catch (e: Exception) {
                uiState = uiState.copy(errorMessage = "Terjadi kesalahan koneksi.", isLoading = false)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerTimeScreen(navController: NavController, viewModel: PrayerViewModel = viewModel()) {
    val uiState = viewModel.uiState
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.updateLocation(context)
    }

    Scaffold(
        containerColor = Color(0xFFF1F8E9)
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            // Header Baru yang Konsisten
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            ) {
                // Background atau Warna Solid dengan Gradasi
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFF4CAF50), Color(0xFF1B5E20))
                            )
                        )
                )
                
                // Navigation Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 8.dp, end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                    }
                    IconButton(onClick = { viewModel.fetchData() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = Color.White)
                    }
                }

                // Title Section
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(24.dp)
                ) {
                    Text(
                        text = "Jadwal Sholat",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = viewModel.currentLocationName,
                        color = Color.White.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(color = Color(0xFF4CAF50))
                } else if (uiState.errorMessage != null) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = uiState.errorMessage, color = Color.Red)
                        Button(onClick = { viewModel.fetchData() }, modifier = Modifier.padding(top = 8.dp)) {
                            Text("Coba Lagi")
                        }
                    }
                } else {
                    uiState.prayerData?.let { jadwal ->
                        val times = listOf(
                            "Imsak" to jadwal.imsak,
                            "Subuh" to jadwal.subuh,
                            "Terbit" to jadwal.terbit,
                            "Dhuha" to jadwal.dhuha,
                            "Dzuhur" to jadwal.dzuhur,
                            "Ashar" to jadwal.ashar,
                            "Maghrib" to jadwal.maghrib,
                            "Isya" to jadwal.isya
                        )

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item {
                                Text(
                                    text = "Lokasi: ${viewModel.currentLocationName}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2E7D32),
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                            }
                            items(times) { (name, time) ->
                                PrayerTimeItem(name, time)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PrayerTimeItem(name: String, time: String) {
    val context = LocalContext.current
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
                Text(
                    text = time,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50)
                )
            }
            IconButton(onClick = { 
                ReminderManager.setPrayerReminder(context, name, time)
            }) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Set Reminder",
                    tint = Color(0xFF4CAF50)
                )
            }
        }
    }
}
