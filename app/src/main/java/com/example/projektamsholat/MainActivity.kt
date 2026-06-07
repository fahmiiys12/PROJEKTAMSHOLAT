package com.example.projektamsholat

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.projektamsholat.ui.theme.SholatAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SholatAppTheme {
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

                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }

                Scaffold(
                    bottomBar = { BottomNavigationBar(navController) },
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { padding ->
                    NavHost(
                        navController = navController,
                        startDestination = "main",
                        modifier = Modifier.padding(padding)
                    ) {
                        composable("main") { SholatHomeScreen(navController, snackbarHostState) }
                        composable("alquran") { QuranScreen(navController) }
                        composable("surah_detail/{surahId}") { backStackEntry ->
                            val surahId = backStackEntry.arguments?.getString("surahId")?.toIntOrNull() ?: 1
                            SurahDetailScreen(surahId, navController)
                        }
                        composable("jadwal_sholat") { PrayerTimeScreen(navController) }
                        composable("kiblat") { KiblatScreen(navController) }
                        composable("kalender") { KalenderScreen(navController) }
                        composable("doa") { DoaScreen(navController) }
                        composable("masjid") { MasjidScreen(navController) }
                        composable("asmaul_husna") { AsmaulHusnaScreen(navController) }
                        composable("tasbih") { TasbihScreen(navController) }
                        composable("donasi") { DetailScreen("Donasi", navController) }
                        composable("salat") { SalatScreen(navController) }
                        composable("ummah") { UmmahScreen(navController) }
                        composable("pesan") { PesanScreen(navController) }
                        composable("saya") { DetailScreen("Saya", navController) }
                    }
                }
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
        Text(text = "Halaman $nama", style = MaterialTheme.typography.headlineMedium, color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Fitur ini sedang dalam pengembangan.", color = Color.Gray)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text("Kembali")
        }
    }
}

@Composable
fun SholatHomeScreen(navController: NavController, snackbarHostState: SnackbarHostState) {
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try {
            // Simulasi Asynchronous (LKP Modul 12)
            delay(2000)
            isLoading = false
            snackbarHostState.showSnackbar("Data sholat berhasil dimuat!")
        } catch (e: Exception) {
            isError = true
            isLoading = false
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color(0xFF00C853))
        }
    } else if (isError) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Gagal Memuat Data", 
                color = Color.Red, 
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                "Pastikan koneksi internet Anda menyala", 
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // 1. Header (Lokasi)
            item {
                HeaderSection()
            }

            // 2. Kartu Jadwal Sholat Utama
            item {
                MainPrayerCard(navController)
            }

            // 3. Baris Ikon Akses Cepat (LazyRow)
            item {
                QuickAccessSection(navController)
            }

            item { 
                Spacer(modifier = Modifier.height(24.dp))
            }

            // 4. Feature List Section
            items(SholatSource.daftarFitur) { fitur ->
                FeatureRowItem(fitur, navController)
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun FeatureRowItem(fitur: FiturIbadah, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { navController.navigate(fitur.rute) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = Color(fitur.warnaDasar).copy(alpha = 0.2f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (fitur.imageUrl != null) {
                        AsyncImage(
                            model = fitur.imageUrl,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize().clip(CircleShape),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(id = R.drawable.banner_sholat),
                            error = painterResource(id = R.drawable.banner_sholat)
                        )
                    } else {
                        Icon(
                            imageVector = fitur.ikon,
                            contentDescription = null,
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
                    color = Color.White
                )
                Text(
                    text = fitur.deskripsi,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "Kecamatan Rajabasa",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun MainPrayerCard(navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(220.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Siluet Masjid (Ganti ke AsyncImage untuk Modul 12)
            AsyncImage(
                model = "https://images.unsplash.com/photo-1591604129939-f1efa4d9f7fa?q=80&w=800&auto=format&fit=crop",
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.15f),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.banner_sholat),
                error = painterResource(id = R.drawable.banner_sholat)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Dhul-Hijjah 21, 1447 AH",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Asr",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.WbSunny,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Text(
                        text = "15:23",
                        style = MaterialTheme.typography.displayLarge,
                        color = Color.White
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Sholat berikutnya 02 : 55 : 42",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Row(
                        modifier = Modifier
                            .clickable { navController.navigate("jadwal_sholat") }
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "waktu salat lainnya",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier
                                .size(16.dp)
                                .background(Color.White.copy(alpha = 0.2f), CircleShape)
                        )
                    }
                }
            }
        }
    }
}

data class QuickFeature(val name: String, val arabicName: String, val imageUrl: String, val route: String)

@Composable
fun QuickAccessSection(navController: NavController) {
    val features = listOf(
        QuickFeature("Quran", "القرآن", "https://images.unsplash.com/photo-1609599006353-e629aaabfeae?q=80&w=200&auto=format&fit=crop", "alquran"),
        QuickFeature("Azkar", "أذكار", "https://images.unsplash.com/photo-1584551271441-705307579684?q=80&w=200&auto=format&fit=crop", "doa"),
        QuickFeature("Masjid", "المساجد", "https://images.unsplash.com/photo-1542623024-a797a7a48c60?q=80&w=200&auto=format&fit=crop", "masjid"),
        QuickFeature("Qibla", "القبلة", "https://images.unsplash.com/photo-1542810634-71277d95dcbb?q=80&w=200&auto=format&fit=crop", "kiblat"),
        QuickFeature("Tasbih", "تسبيح", "https://images.unsplash.com/photo-1564121211835-e88c852648ab?q=80&w=200&auto=format&fit=crop", "tasbih")
    )

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(features) { feature ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { navController.navigate(feature.route) }
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(Color.Transparent, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                   AsyncImage(
                       model = feature.imageUrl,
                       contentDescription = null,
                       modifier = Modifier.size(48.dp).clip(CircleShape),
                       contentScale = ContentScale.Crop,
                       placeholder = painterResource(id = R.drawable.banner_sholat),
                       error = painterResource(id = R.drawable.banner_sholat)
                   )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = feature.arabicName,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp),
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = feature.name,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("Utama", Icons.Default.Home, "main"),
        BottomNavItem("Salat", Icons.AutoMirrored.Filled.MenuBook, "salat"),
        BottomNavItem("Ummah", Icons.Default.Language, "ummah"),
        BottomNavItem("Pesan", Icons.AutoMirrored.Filled.Chat, "pesan"),
        BottomNavItem("Saya", Icons.Default.Person, "saya")
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        tonalElevation = 8.dp
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title, fontSize = 10.sp) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo("main") { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.secondary,
                    selectedTextColor = MaterialTheme.colorScheme.secondary,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

data class BottomNavItem(val title: String, val icon: ImageVector, val route: String)
