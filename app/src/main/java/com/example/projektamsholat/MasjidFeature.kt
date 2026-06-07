package com.example.projektamsholat

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

data class Masjid(
    val nama: String,
    val alamat: String,
    val jarak: String,
    val rating: Float
)

object MasjidSource {
    val daftarMasjid = listOf(
        Masjid("Masjid Istiqlal", "Jl. Taman Wijaya Kusuma, Jakarta Pusat", "2.5 km", 4.9f),
        Masjid("Masjid Agung Al-Azhar", "Jl. Sisingamangaraja, Jakarta Selatan", "5.1 km", 4.8f),
        Masjid("Masjid Cut Meutia", "Jl. Taman Cut Mutia No.1, Jakarta Pusat", "3.2 km", 4.7f),
        Masjid("Masjid At-Tin", "Jl. Raya Jakarta-Bogor, Jakarta Timur", "8.5 km", 4.8f),
        Masjid("Masjid Ramlie Musofa", "Jl. Danau Sunter Utara, Jakarta Utara", "10.2 km", 4.9f)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MasjidScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var currentAddress by remember { mutableStateOf("Mendeteksi lokasi...") }

    LaunchedEffect(Unit) {
        scope.launch {
            currentAddress = LocationHelper.getCurrentLocationName(context)
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
                // Header Baru yang Konsisten
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                ) {
                    // Background Gradient
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color(0xFF263238), Color(0xFF121212))
                                )
                            )
                    )
                    
                    // Decorative Icon in background
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier
                            .size(150.dp)
                            .align(Alignment.CenterEnd)
                            .offset(x = 20.dp, y = 20.dp),
                        tint = Color.White.copy(alpha = 0.1f)
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
                            text = "Masjid Terdekat",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Lokasi Anda: $currentAddress",
                            color = Color.White.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            items(MasjidSource.daftarMasjid) { masjid ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                    MasjidItem(masjid)
                }
            }
        }
    }
}

@Composable
fun MasjidItem(masjid: Masjid) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(60.dp),
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFF00C853))
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = masjid.nama,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = masjid.alamat,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = Color(0xFFFFC107)
                    )
                    Text(
                        text = " ${masjid.rating} • ${masjid.jarak}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00C853)
                    )
                }
            }
        }
    }
}
