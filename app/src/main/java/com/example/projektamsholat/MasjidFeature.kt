package com.example.projektamsholat

import android.content.Context
import android.location.Location
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.util.Locale

data class Masjid(
    val nama: String,
    val alamat: String,
    val jarak: String,
    val rating: Float,
    val lat: Double = 0.0,
    val lon: Double = 0.0
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MasjidScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var currentAddress by remember { mutableStateOf("Mendeteksi lokasi...") }
    var daftarMasjid by remember { mutableStateOf<List<Masjid>>(emptyList()) }
    var isSearching by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        scope.launch {
            val location = LocationHelper.getCurrentLocation(context)
            if (location != null) {
                currentAddress = LocationHelper.getCurrentLocationName(context)
                // Ambil data masjid dari API berdasarkan lokasi Lampung/Sekitarnya
                val masjids = fetchNearbyMasjids(location)
                daftarMasjid = masjids
            } else {
                currentAddress = "Lokasi tidak ditemukan"
            }
            isSearching = false
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
                        .height(220.dp)
                        .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color(0xFF263238), Color(0xFF121212))
                                )
                            )
                    )
                    
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier
                            .size(150.dp)
                            .align(Alignment.CenterEnd)
                            .offset(x = 20.dp, y = 20.dp),
                        tint = Color.White.copy(alpha = 0.1f)
                    )

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

            if (isSearching) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF00C853))
                    }
                }
            } else if (daftarMasjid.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("Tidak menemukan masjid di sekitar Anda", color = Color.Gray)
                    }
                }
            } else {
                items(daftarMasjid) { masjid ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                        MasjidItem(masjid)
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}

@Composable
fun MasjidItem(masjid: Masjid) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(50.dp),
                shape = CircleShape,
                color = Color(0xFF00C853).copy(alpha = 0.1f)
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
                    Icon(Icons.Default.Star, null, modifier = Modifier.size(14.dp), tint = Color(0xFFFFC107))
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

suspend fun fetchNearbyMasjids(userLocation: Location): List<Masjid> = withContext(Dispatchers.IO) {
    try {
        // Query Overpass API untuk mencari masjid dalam radius 5km dari lokasi user
        val urlString = "https://overpass-api.de/api/interpreter?data=[out:json];node[\"amenity\"=\"place_of_worship\"][\"religion\"=\"muslim\"](around:5000,${userLocation.latitude},${userLocation.longitude});out;"
        val response = URL(urlString).readText()
        val json = JSONObject(response)
        val elements = json.getJSONArray("elements")
        
        val results = mutableListOf<Masjid>()
        for (i in 0 until minOf(elements.length(), 10)) {
            val obj = elements.getJSONObject(i)
            val tags = obj.optJSONObject("tags")
            val name = tags?.optString("name") ?: "Masjid Tanpa Nama"
            val lat = obj.getDouble("lat")
            val lon = obj.getDouble("lon")
            
            // Hitung jarak
            val masjidLocation = Location("").apply {
                latitude = lat
                longitude = lon
            }
            val distanceInMeters = userLocation.distanceTo(masjidLocation)
            val distanceString = if (distanceInMeters < 1000) {
                "${distanceInMeters.toInt()} m"
            } else {
                String.format(Locale.US, "%.1f km", distanceInMeters / 1000)
            }

            results.add(Masjid(
                nama = name,
                alamat = tags?.optString("addr:street") ?: "Sekitar lokasi Anda",
                jarak = distanceString,
                rating = 4.5f + (i % 5) * 0.1f // Mock rating agar bervariasi
            ))
        }
        results.sortBy { it.jarak }
        results
    } catch (e: Exception) {
        emptyList()
    }
}
