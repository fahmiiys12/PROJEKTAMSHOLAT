package com.example.projektamsholat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class Doa(val judul: String, val arab: String, val latin: String, val arti: String)

val daftarDoa = listOf(
    Doa(
        "Doa Sebelum Makan",
        "اللَّهُمَّ بَارِكْ لَنَا فِيمَا رَزَقْتَنَا وَقِنَا عَذَابَ النَّارِ",
        "Allohumma barik lana fiima rozaktana waqina 'adzaban naar",
        "Ya Allah, berkahilah kami atas rezeki yang telah Engkau berikan kepada kami dan jagalah kami dari siksa api neraka."
    ),
    Doa(
        "Doa Sesudah Makan",
        "الْحَمْدُ لِلَّهِ الَّذِي أَطْعَمَنَا وَسَقَانَا وَجَعَلَنَا مُسْلِمِينَ",
        "Alhamdulillahilladzi ath'amana wasaqana waja'alana muslimin",
        "Segala puji bagi Allah yang telah memberi kami makan dan minum, serta menjadikan kami muslim."
    ),
    Doa(
        "Doa Sebelum Tidur",
        "بِاسْمِكَ اللَّهُمَّ أَحْيَا وَأَمُوتُ",
        "Bismikallahumma ahya wa amuutu",
        "Dengan nama-Mu ya Allah aku hidup dan aku mati."
    ),
    Doa(
        "Doa Bangun Tidur",
        "الْحَمْدُ لِلَّهِ الَّذِي أَحْيَانَا بَعْدَ مَا أَمَاتَنَا وَإِلَيْهِ النُّشُورُ",
        "Alhamdulillahilladzi ahyana ba'da ma amatana wa ilaihin nushur",
        "Segala puji bagi Allah yang telah menghidupkan kami setelah mematikan kami dan kepada-Nya lah kami kembali."
    ),
    Doa(
        "Doa Kedua Orang Tua",
        "رَبِّ اغْفِرْ لِي وَلِوَالِدَيَّ وَارْحَمْهُمَا كَمَا رَبَّيَانِي صَغِيرًا",
        "Rabbighfir li wa liwaalidayya warhamhuma kamaa rabbayaani shaghiiraa",
        "Tuhanku, ampunilah dosaku dan dosa kedua orang tuaku, dan sayangilah keduanya sebagaimana mereka menyayangiku di waktu kecil."
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoaScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredDoa = remember(searchQuery) {
        if (searchQuery.isEmpty()) {
            daftarDoa
        } else {
            daftarDoa.filter { 
                it.judul.contains(searchQuery, ignoreCase = true) || 
                it.arti.contains(searchQuery, ignoreCase = true)
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
                        .height(220.dp)
                        .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.doa_sholat),
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
                            text = "Doa & Dzikir",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Kumpulan doa harian dan dzikir",
                            color = Color.White.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    placeholder = { Text("Cari Doa (contoh: Makan atau Tidur)", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Hapus", tint = Color.Gray)
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF00C853),
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                        cursorColor = Color(0xFF00C853)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }

            items(filteredDoa) { doa ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                    DoaCard(doa)
                }
            }

            if (filteredDoa.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("Doa tidak ditemukan", color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
fun DoaCard(doa: Doa) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = doa.judul,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00C853)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = doa.arab,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxWidth(),
                lineHeight = 42.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = doa.latin,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF69F0AE)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = doa.arti,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}
