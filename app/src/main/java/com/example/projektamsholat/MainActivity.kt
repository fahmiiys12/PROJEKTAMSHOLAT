package com.example.projektamsholat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF5F5F5)) {
                    SholatApp()
                }
            }
        }
    }
}

@Composable
fun SholatApp() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // SYARAT: Text & Modifier (Kerapihan Layout)
        Text(
            text = "JADWAL SHOLAT HARI INI",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2E7D32),
            modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)
        )

        // SYARAT: Menampilkan 3-5 data (Daftar Sholat)
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(SholatSource.daftarSholat) { sholat ->
                SholatItem(sholat)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Footer Informasi (Component: Card & Column)
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Fahmi Isma Yuda",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF1B5E20)
                )
                Text(
                    text = "NPM: 2417051062",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
        }
    }
}

@Composable
fun SholatItem(sholat: JadwalSholat) {
    // SYARAT: Layout Row/Column & Modifier (Padding, Kerapihan)
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // SYARAT: Image (Menggunakan ikon vektor yang sesuai data)
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color(0xFFC8E6C9), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberVectorPainter(image = sholat.ikon),
                    contentDescription = sholat.nama,
                    modifier = Modifier.size(30.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // SYARAT: Layout Column & Text
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = sholat.nama,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = sholat.waktu,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            // SYARAT: Button
            Button(
                onClick = { /* Aksi klik */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (sholat.status == "Sudah") Color(0xFF4CAF50) else Color(0xFFFF9800)
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = sholat.status,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
