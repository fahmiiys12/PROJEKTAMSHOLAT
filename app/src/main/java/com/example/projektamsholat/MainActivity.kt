package com.example.projektamsholat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
                SholatApp()
            }
        }
    }
}

@Composable
fun SholatApp() {
    Column(modifier = Modifier.padding(20.dp)) {
        Text(
            text = "DAFTAR SHOLAT HARI INI",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Mengambil data dari SholatSource.daftarSholat
        SholatSource.daftarSholat.forEach { sholat ->
            Text(
                text = "${sholat.nama} - ${sholat.waktu} (${sholat.status})",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 8.dp),
                color = if (sholat.status == "Sudah") Color(0xFF0000FF) else Color(0xFFFF0000)
            )
        }
        
        Spacer(modifier = Modifier.height(30.dp))
        Text(text = "Oleh: Fahmi Isma Yuda - 2417051062", color = Color.Gray)
    }
}
