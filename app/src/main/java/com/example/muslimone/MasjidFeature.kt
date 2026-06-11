package com.example.muslimone

import android.content.Context
import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
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
import com.example.muslimone.ui.DetailHeader
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.muslimone.data.api.MasjidApiService
import com.example.muslimone.utils.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.util.Locale
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.alpha

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
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var refreshTrigger by remember { mutableStateOf(0) }

    LaunchedEffect(refreshTrigger) {
        isSearching = true
        errorMessage = null
        
        if (!NetworkUtils.isInternetAvailable(context)) {
            errorMessage = "Tidak ada koneksi internet. Aktifkan data untuk mencari masjid."
            isSearching = false
            return@LaunchedEffect
        }

        try {
            val location = LocationHelper.getCurrentLocation(context)
            if (location != null) {
                currentAddress = LocationHelper.getCurrentLocationName(context)
                val masjids = fetchNearbyMasjids(location)
                daftarMasjid = masjids
                if (masjids.isEmpty()) {
                    errorMessage = "Tidak ada masjid ditemukan dalam radius 20km."
                }
            } else {
                currentAddress = "Lokasi tidak ditemukan"
                errorMessage = "Gagal mendapatkan koordinat GPS. Pastikan GPS aktif."
            }
        } catch (e: Exception) {
            errorMessage = "Terjadi kesalahan saat memuat data: ${e.localizedMessage}"
        } finally {
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
                DetailHeader(
                    title = "Masjid Terdekat",
                    subtitle = currentAddress,
                    imageSource = R.drawable.masjid_sholat,
                    onBack = { navController.popBackStack() }
                )
            }

            if (isSearching) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(80.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF00C853))
                    }
                }
            } else if (errorMessage != null) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.LocationOn, null, tint = Color.Gray, modifier = Modifier.size(64.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = errorMessage!!, 
                            color = Color.Gray, 
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )
                        Button(
                            onClick = { refreshTrigger++ },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Coba Lagi")
                        }
                    }
                }
            } else {
                items(daftarMasjid) { masjid ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                        MasjidItem(masjid)
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(30.dp)) }
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
                modifier = Modifier.size(45.dp),
                shape = CircleShape,
                color = Color(0xFF00C853).copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFF00C853), modifier = Modifier.size(20.dp))
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
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                    Icon(Icons.Default.Star, null, modifier = Modifier.size(14.dp), tint = Color(0xFFFFC107))
                    Text(
                        text = " ${String.format("%.1f", masjid.rating)} • ${masjid.jarak}",
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
        val apiService = MasjidApiService.create()
        
        val lat = String.format(Locale.US, "%.6f", userLocation.latitude)
        val lon = String.format(Locale.US, "%.6f", userLocation.longitude)
        
        val query = """
            [out:json][timeout:25];
            (
              node["amenity"="place_of_worship"]["religion"="muslim"](around:20000,$lat,$lon);
              way["amenity"="place_of_worship"]["religion"="muslim"](around:20000,$lat,$lon);
              relation["amenity"="place_of_worship"]["religion"="muslim"](around:20000,$lat,$lon);
              node["building"="mosque"](around:20000,$lat,$lon);
              way["building"="mosque"](around:20000,$lat,$lon);
            );
            out center;
        """.trimIndent()
        
        val response = apiService.getNearbyMasjids(query)
        val json = JSONObject(response)
        val elements = json.getJSONArray("elements")
        
        val results = mutableListOf<Masjid>()
        for (i in 0 until elements.length()) {
            val obj = elements.getJSONObject(i)
            val tags = obj.optJSONObject("tags") ?: continue
            val name = tags.optString("name").ifEmpty { 
                if (tags.optString("amenity") == "place_of_worship") "Masjid / Musholla" else "Masjid"
            }
            
            val lat = if (obj.has("lat")) obj.getDouble("lat") else obj.optJSONObject("center")?.optDouble("lat") ?: continue
            val lon = if (obj.has("lon")) obj.getDouble("lon") else obj.optJSONObject("center")?.optDouble("lon") ?: continue
            
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
                alamat = tags.optString("addr:street", "Sekitar lokasi Anda"),
                jarak = distanceString,
                rating = 4.5f + (i % 5) * 0.1f,
                lat = lat,
                lon = lon
            ))
        }
        results.sortBy { 
            if (it.jarak.contains("km")) it.jarak.replace(" km", "").toDouble() * 1000 
            else it.jarak.replace(" m", "").toDouble()
        }
        results
    } catch (e: Exception) {
        throw e
    }
}
