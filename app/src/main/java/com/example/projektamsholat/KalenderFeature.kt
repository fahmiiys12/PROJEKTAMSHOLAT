package com.example.projektamsholat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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

// Additional API Models for Monthly Schedule
data class MonthlyApiResponse(val status: Boolean, val data: MonthlyPrayerData)
data class MonthlyPrayerData(val id: String, val lokasi: String, val daerah: String, val jadwal: List<Jadwal>)

interface KalenderApiService {
    @GET("sholat/jadwal/1301/{tahun}/{bulan}")
    suspend fun getJadwalBulanan(
        @Path("tahun") tahun: String,
        @Path("bulan") bulan: String
    ): MonthlyApiResponse
}

object KalenderRetrofitClient {
    private const val BASE_URL = "https://api.myquran.com/v2/"
    val service: KalenderApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KalenderApiService::class.java)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KalenderScreen(navController: NavController) {
    var monthlyJadwal by remember { mutableStateOf<List<Jadwal>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var currentMonthName by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val calendar = Calendar.getInstance()
                val tahun = calendar.get(Calendar.YEAR).toString()
                val bulan = String.format("%02d", calendar.get(Calendar.MONTH) + 1)
                currentMonthName = SimpleDateFormat("MMMM yyyy", Locale("id", "ID")).format(calendar.time)

                val response = KalenderRetrofitClient.service.getJadwalBulanan(tahun, bulan)
                if (response.status) {
                    monthlyJadwal = response.data.jadwal
                } else {
                    errorMessage = "Gagal memuat kalender sholat."
                }
            } catch (e: Exception) {
                errorMessage = "Terjadi kesalahan: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        containerColor = Color(0xFF121212)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                ) {
                    // Pakai Banner atau Warna Gradasi karena tidak ada icon spesifik
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color(0xFFE65100), Color(0xFF121212))
                                )
                            )
                    )
                    
                    // Navigation Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, start = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                        }
                    }

                    // Title Section
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(24.dp)
                    ) {
                        Text(
                            text = "Kalender Sholat",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = currentMonthName,
                            color = Color.White.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (isLoading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF00C853))
                    }
                }
            } else if (errorMessage != null) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        Text(text = errorMessage!!, color = Color.Red)
                    }
                }
            } else {
                items(monthlyJadwal) { jadwal ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                        KalenderItem(jadwal)
                    }
                }
            }
        }
    }
}

@Composable
fun KalenderItem(jadwal: Jadwal) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = jadwal.tanggal,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF00C853),
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SholatTimeSmall("Subuh", jadwal.subuh)
                SholatTimeSmall("Dzuhur", jadwal.dzuhur)
                SholatTimeSmall("Ashar", jadwal.ashar)
                SholatTimeSmall("Maghrib", jadwal.maghrib)
                SholatTimeSmall("Isya", jadwal.isya)
            }
        }
    }
}

@Composable
fun SholatTimeSmall(label: String, time: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 10.sp, color = Color.Gray)
        Text(text = time, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }
}
