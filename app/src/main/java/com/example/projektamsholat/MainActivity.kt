package com.example.projektamsholat

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// --- 1. DATA MODEL (Kriteria Wajib Poin 1) ---
data class Sholat(
    val id: Int,
    val nama: String,
    val waktu: String,
    val deskripsi: String,
    val imageRes: Int
)

data class Amalan(
    val id: Int,
    val judul: String,
    val imageRes: Int
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val context = LocalContext.current
                
                // Izin Notifikasi & Lokasi
                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions(),
                    onResult = { _ -> }
                )

                LaunchedEffect(Unit) {
                    val permissions = mutableListOf<String>()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissions.add(Manifest.permission.POST_NOTIFICATIONS)
                    }
                    permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
                    permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
                    permissionLauncher.launch(permissions.toTypedArray())
                }

                Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF1F8E9)) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "main") {
                        composable("main") { SholatApp(navController) }
                        composable("alquran") { QuranScreen(navController) }
                        composable("jadwal_sholat") { PrayerTimeScreen(navController) }
                        composable("kiblat") { KiblatScreen(navController) }
                        composable("kalender") { KalenderScreen(navController) }
                        composable("doa") { DoaScreen(navController) }
                        composable("asmaul_husna") { AsmaulHusnaScreen(navController) }
                        composable("donasi") { DetailScreen("Donasi", navController) }
                        composable("masjid") { MasjidScreen(navController) }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SholatApp(navController: NavController) {
    // --- DUMMY DATA (Kriteria Wajib Poin 1) ---
    val daftarAmalan = remember {
        listOf(
            Amalan(1, "Sedekah Subuh", R.drawable.donasi_sholat),
            Amalan(2, "Baca Al-Waqiah", R.drawable.alquran_sholat),
            Amalan(3, "Dzikir Pagi", R.drawable.doa_sholat),
            Amalan(4, "Sholat Tahajud", R.drawable.banner_sholat)
        )
    }

    val daftarSholat = remember {
        listOf(
            Sholat(1, "Subuh", "04:35", "Awal hari yang penuh berkah dengan sholat fajar.", R.drawable.banner_sholat),
            Sholat(2, "Dzuhur", "12:00", "Istirahat sejenak untuk mengingat Allah di tengah hari.", R.drawable.banner_sholat),
            Sholat(3, "Ashar", "15:20", "Penyemangat ibadah di waktu sore hari.", R.drawable.banner_sholat),
            Sholat(4, "Maghrib", "18:05", "Menyambut malam dengan penuh kesyukuran.", R.drawable.banner_sholat),
            Sholat(5, "Isya", "19:15", "Sholat penutup yang menenangkan hati sebelum tidur.", R.drawable.banner_sholat)
        )
    }

    Scaffold(
        containerColor = Color(0xFFF1F8E9),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("PENGINGAT SHOLAT", fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF2E7D32),
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            Surface(
                color = Color(0xFFC8E6C9),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                shadowElevation = 8.dp
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Tugas Modul 6 - Praktikum Mobile",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color(0xFF1B5E20)
                    )
                }
            }
        }
    ) { padding ->
        // --- 2. STRUKTUR LAYOUT: LAZYCOLUMN (Kriteria Wajib Poin 2) ---
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
        ) {
            
            // --- 2. ITEM HEADER: LAZYROW (Kriteria Wajib Poin 2) ---
            item {
                Text(
                    text = "Rekomendasi Amalan Sunnah",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32),
                    modifier = Modifier.padding(bottom = 12.dp, start = 4.dp)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(daftarAmalan) { amalan ->
                        // --- 3. KOMPONEN CARD (Kriteria Wajib Poin 3) ---
                        Card(
                            modifier = Modifier.width(150.dp),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    painter = painterResource(id = amalan.imageRes),
                                    contentDescription = null,
                                    modifier = Modifier.size(90.dp).clip(RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = amalan.judul,
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    color = Color(0xFF1B5E20)
                                )
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Jadwal Sholat 5 Waktu",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32),
                    modifier = Modifier.padding(top = 8.dp, start = 4.dp)
                )
            }

            // --- 2. BLOK ITEMS() UNTUK DAFTAR UTAMA (Kriteria Wajib Poin 2) ---
            items(daftarSholat) { sholat ->
                // --- 3. DESAIN KOMPONEN ITEM (Kriteria Wajib Poin 3) ---
                Card(
                    onClick = { navController.navigate("jadwal_sholat") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    // Tata letak menggunakan Row dan Column (Kriteria Wajib Poin 3)
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = sholat.imageRes),
                            contentDescription = null,
                            modifier = Modifier.size(85.dp).clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = sholat.nama,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1B5E20)
                            )
                            Text(
                                text = sholat.waktu,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFF2E7D32),
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = sholat.deskripsi,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.DarkGray,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(12.dp))
                            
                            // Button Aksi (Kriteria Wajib Poin 3)
                            val context = LocalContext.current
                            Button(
                                onClick = { 
                                    ReminderManager.setPrayerReminder(context, sholat.nama, sholat.waktu)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Text("Set Pengingat", fontSize = 12.sp, color = Color.White)
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Menu Utama",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32),
                    modifier = Modifier.padding(top = 8.dp, start = 4.dp)
                )
            }

            // Integrasi Fitur Ibadah (Tetap dipertahankan dari versi sebelumnya)
            items(SholatSource.daftarFitur) { fitur ->
                FiturCard(fitur, navController)
            }
        }
    }
}

@Composable
fun DetailScreen(nama: String, navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Halaman $nama", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Fitur ini sedang dalam pengembangan.")
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text("Kembali")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiturCard(fitur: FiturIbadah, navController: NavController) {
    Card(
        onClick = { navController.navigate(fitur.rute) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(56.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color.Transparent
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(fitur.warnaDasar).copy(alpha = 0.2f),
                                    Color(fitur.warnaDasar).copy(alpha = 0.05f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (fitur.gambarResId != null) {
                        Image(
                            painter = painterResource(id = fitur.gambarResId),
                            contentDescription = null,
                            modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        Icon(
                            imageVector = fitur.ikon,
                            contentDescription = null,
                            modifier = Modifier.size(30.dp),
                            tint = Color(fitur.warnaDasar)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = fitur.nama,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B5E20)
                )
                Text(
                    text = fitur.deskripsi,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFF4CAF50)
            )
        }
    }
}
