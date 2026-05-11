package PROJEKTAMSHOLAT

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    GreetingSection()
                }
            }
        }
    }
}

@Composable
fun GreetingSection() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Halo, saya Fahmi Isma Yuda - 2417051062", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Ide Projek: Pengingat Sholat & Progres Ibadah")
    }
}