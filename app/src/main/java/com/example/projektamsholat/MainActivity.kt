package com.example.projektamsholat

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.projektamsholat.data.api.ApiService
import com.example.projektamsholat.data.repository.SholatRepository
import com.example.projektamsholat.ui.viewmodel.SholatViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.remember
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

                // Inisialisasi MVVM Components
                val apiService = remember { ApiService.create() }
                val repository = remember { SholatRepository(apiService) }
                val sholatViewModel: SholatViewModel = viewModel(
                    factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                            return SholatViewModel(repository) as T
                        }
                    }
                )

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
                        composable("salat") { SalatScreen(navController, sholatViewModel) }
                        composable("ummah") { UmmahScreen(navController) }
                        composable("pesan") { PesanScreen(navController) }
                        composable("saya") { ProfileScreen(navController) }
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
            delay(1000)
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
            Text("Gagal Memuat Data", color = Color.Red, fontWeight = FontWeight.Bold)
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            item { 
                var userLocation by remember { mutableStateOf("Mendeteksi lokasi...") }
                val context = LocalContext.current
                LaunchedEffect(Unit) {
                    userLocation = LocationHelper.getCurrentLocationName(context)
                }
                HeaderSection(userLocation) 
            }
            item { MainPrayerCard(navController) }
            item { QuickAccessSection(navController) }
            item { Spacer(modifier = Modifier.height(24.dp)) }
            items(SholatSource.daftarFitur) { fitur ->
                FeatureRowItem(fitur, navController)
            }
            item { Spacer(modifier = Modifier.height(20.dp)) }
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
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
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
                        Icon(fitur.ikon, null, tint = Color(fitur.warnaDasar))
                    }
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(fitur.nama, fontWeight = FontWeight.Bold, color = Color.White)
                Text(fitur.deskripsi, fontSize = 12.sp, color = Color.Gray)
            }
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = Color.Gray)
        }
    }
}

@Composable
fun HeaderSection(locationName: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.LocationOn, null, tint = Color.White, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(locationName, color = Color.White, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun MainPrayerCard(navController: NavController) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp).height(200.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = "https://images.unsplash.com/photo-1591604129939-f1efa4d9f7fa?q=80&w=800&auto=format&fit=crop",
                contentDescription = null,
                modifier = Modifier.fillMaxSize().alpha(0.15f),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.banner_sholat)
            )
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Dhul-Hijjah 21, 1447 AH", color = Color.White.copy(alpha = 0.8f))
                Spacer(modifier = Modifier.height(16.dp))
                Text("Asr", style = MaterialTheme.typography.headlineMedium, color = Color.White)
                Text("15:23", style = MaterialTheme.typography.displayLarge, color = Color.White)
            }
        }
    }
}

@Composable
fun QuickAccessSection(navController: NavController) {
    // Implementasi sederhana LazyRow untuk akses cepat
    Row(modifier = Modifier.padding(16.dp)) {
        Text("Akses Cepat", color = Color.White, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("Utama", Icons.Default.Home, "main"),
        BottomNavItem("Salat", Icons.Default.Timer, "salat"),
        BottomNavItem("Ummah", Icons.Default.Groups, "ummah"),
        BottomNavItem("Pesan", Icons.AutoMirrored.Filled.Chat, "pesan"),
        BottomNavItem("Saya", Icons.Default.Person, "saya")
    )
    NavigationBar(
        containerColor = Color.Black,
        contentColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, null) },
                label = { Text(item.title, fontSize = 10.sp) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo("main") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF00C853),
                    selectedTextColor = Color(0xFF00C853),
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

data class BottomNavItem(val title: String, val icon: ImageVector, val route: String)
