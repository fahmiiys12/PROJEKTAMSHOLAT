package com.example.projektamsholat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerTimeScreen(navController: NavController) {
    var prayerData by remember { mutableStateOf<Jadwal?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    fun fetchData() {
        scope.launch {
            isLoading = true
            errorMessage = null
            try {
                val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                val currentDate = sdf.format(Date()).split("/")
                val response = RetrofitClient.service.getJadwal(currentDate[0], currentDate[1], currentDate[2])
                if (response.status) {
                    prayerData = response.data.jadwal
                } else {
                    errorMessage = "Gagal mengambil data dari server."
                }
            } catch (e: Exception) {
                errorMessage = "Terjadi kesalahan koneksi."
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(Unit) {
        fetchData()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Jadwal Sholat") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    IconButton(onClick = { fetchData() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF4CAF50),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
            if (isLoading) {
                CircularProgressIndicator(color = Color(0xFF4CAF50))
            } else if (errorMessage != null) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = errorMessage!!, color = Color.Red)
                    Button(onClick = { fetchData() }, modifier = Modifier.padding(top = 8.dp)) {
                        Text("Coba Lagi")
                    }
                }
            } else {
                prayerData?.let { jadwal ->
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
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                text = "Kota Jakarta - ${jadwal.tanggal}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp)
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

@Composable
fun PrayerTimeItem(name: String, time: String) {
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
            Text(text = name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
            Text(
                text = time,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4CAF50)
            )
        }
    }
}
