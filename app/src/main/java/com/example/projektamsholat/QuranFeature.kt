package com.example.projektamsholat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

data class Surah(val nomor: Int, val nama: String, val arti: String, val jumlahAyat: Int)

val daftarSurah = listOf(
    Surah(1, "Al-Fatihah", "Pembukaan", 7),
    Surah(2, "Al-Baqarah", "Sapi Betina", 286),
    Surah(3, "Ali 'Imran", "Keluarga 'Imran", 200),
    Surah(4, "An-Nisa'", "Wanita", 176),
    Surah(5, "Al-Ma'idah", "Hidangan", 120),
    // ... add more as needed
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Al-Qur'an Digital") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2196F3),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(daftarSurah) { surah ->
                SurahItem(surah)
            }
        }
    }
}

@Composable
fun SurahItem(surah: Surah) {
    ListItem(
        headlineContent = { Text(surah.nama, fontWeight = FontWeight.Bold) },
        supportingContent = { Text("${surah.arti} • ${surah.jumlahAyat} Ayat") },
        leadingContent = {
            Surface(
                shape = MaterialTheme.shapes.small,
                color = Color(0xFF2196F3).copy(alpha = 0.1f),
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = surah.nomor.toString(),
                        color = Color(0xFF2196F3),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        modifier = Modifier.clickable { /* Handle click to read Surah */ }
    )
    Divider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
}
