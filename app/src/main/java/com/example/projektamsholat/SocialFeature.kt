package com.example.projektamsholat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UmmahScreen(navController: NavController) {
    Scaffold(
        containerColor = Color(0xFF121212),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Ummah Community", color = Color.White) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Black),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text("Postingan Terbaru", style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
            }
            
            items(listOf(
                "Mari kita perbanyak sholawat di hari Jumat ini.",
                "Subhanallah, pemandangan Masjid Nabawi pagi ini sangat indah.",
                "Ada yang tahu jadwal kajian di daerah Rajabasa?",
                "Alhamdulillah, target tilawah hari ini tercapai."
            )) { post ->
                PostItem(post)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PesanScreen(navController: NavController) {
    Scaffold(
        containerColor = Color(0xFF121212),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Pesan", color = Color.White) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Black),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            items(listOf(
                "Admin" to "Selamat datang di Projektam Sholat!",
                "Budi" to "Besok jadi ke Masjid?",
                "Siti" to "Terima kasih infonya.",
                "Komunitas" to "Jadwal kajian minggu ini..."
            )) { chat ->
                ChatItem(chat.first, chat.second)
            }
        }
    }
}

@Composable
fun PostItem(content: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(40.dp).background(Color(0xFF00C853), CircleShape))
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("User Sholeh", color = Color.White, fontWeight = FontWeight.Bold)
                    Text("2 jam yang lalu", color = Color.Gray, fontSize = 12.sp)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(content, color = Color.White)
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Icon(Icons.Default.FavoriteBorder, contentDescription = null, tint = Color.Gray)
                Icon(Icons.AutoMirrored.Filled.Comment, contentDescription = null, tint = Color.Gray)
                Icon(Icons.Default.Share, contentDescription = null, tint = Color.Gray)
            }
        }
    }
}

@Composable
fun ChatItem(name: String, lastMsg: String) {
    ListItem(
        headlineContent = { Text(name, color = Color.White, fontWeight = FontWeight.Bold) },
        supportingContent = { Text(lastMsg, color = Color.Gray) },
        leadingContent = {
            Box(modifier = Modifier.size(48.dp).background(Color.Gray.copy(alpha = 0.3f), CircleShape), contentAlignment = Alignment.Center) {
                Text(name.take(1), color = Color.White)
            }
        },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
    )
    HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
}
