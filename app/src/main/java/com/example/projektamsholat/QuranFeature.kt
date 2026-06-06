package com.example.projektamsholat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
        containerColor = Color(0xFFF1F8E9)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                DetailHeader(
                    title = "Al-Qur'an Digital",
                    subtitle = "Baca dan Pelajari Kitab Suci",
                    imageRes = R.drawable.alquran_sholat,
                    color = Color(0xFF2196F3),
                    onBack = { navController.popBackStack() }
                )
            }

            items(daftarSurah) { surah ->
                SurahItem(surah)
            }
        }
    }
}

@Composable
fun DetailHeader(
    title: String,
    subtitle: String,
    imageRes: Int,
    color: Color,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        // Gradient Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.3f), Color.Black.copy(alpha = 0.7f))
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
            IconButton(onClick = onBack) {
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
                text = title,
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = subtitle,
                color = Color.White.copy(alpha = 0.8f),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
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
