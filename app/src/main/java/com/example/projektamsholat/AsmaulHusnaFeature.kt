package com.example.projektamsholat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class AsmaulHusna(val nomor: Int, val arab: String, val latin: String, val arti: String)

val daftarAsmaulHusna = listOf(
    AsmaulHusna(1, "الرَّحْمَنُ", "Ar-Rahman", "Maha Pengasih"),
    AsmaulHusna(2, "الرَّحِيمُ", "Ar-Rahim", "Maha Penyayang"),
    AsmaulHusna(3, "الْمَلِكُ", "Al-Malik", "Maha Merajai"),
    AsmaulHusna(4, "الْقُدُّوسُ", "Al-Quddus", "Maha Suci"),
    AsmaulHusna(5, "السَّلاَمُ", "As-Salam", "Maha Memberi Kesejahteraan"),
    AsmaulHusna(6, "الْمُؤْمِنُ", "Al-Mu'min", "Maha Memberi Keamanan"),
    AsmaulHusna(7, "الْمُهَيْمِنُ", "Al-Muhaimin", "Maha Pemelihara"),
    AsmaulHusna(8, "الْعَزِيزُ", "Al-Aziz", "Maha Perkasa"),
    AsmaulHusna(9, "الْجَبَّارُ", "Al-Jabbar", "Maha Memaksa"),
    AsmaulHusna(10, "الْمُتَكَبِّرُ", "Al-Mutakabbir", "Maha Memiliki Kebesaran"),
    AsmaulHusna(11, "الْخَالِقُ", "Al-Khaliq", "Maha Pencipta"),
    AsmaulHusna(12, "الْبَارِئُ", "Al-Bari'", "Maha Melepaskan"),
    AsmaulHusna(13, "الْمُصَوِّرُ", "Al-Mushawwir", "Maha Membentuk Rupa"),
    AsmaulHusna(14, "الْغَفَّارُ", "Al-Ghaffar", "Maha Pengampun"),
    AsmaulHusna(15, "الْقَهَّارُ", "Al-Qahhar", "Maha Menundukkan"),
    AsmaulHusna(16, "الْوَهَّابُ", "Al-Wahhab", "Maha Pemberi Karunia"),
    AsmaulHusna(17, "الرَّزَّاقُ", "Ar-Razzaq", "Maha Pemberi Rezeki"),
    AsmaulHusna(18, "الْفَتَّاحُ", "Al-Fattah", "Maha Pembuka Rahmat"),
    AsmaulHusna(19, "الْعَلِيمُ", "Al-Alim", "Maha Mengetahui"),
    AsmaulHusna(20, "الْقَابِضُ", "Al-Qabidh", "Maha Menyempitkan"),
    AsmaulHusna(21, "الْبَاسِطُ", "Al-Basit", "Maha Melapangkan"),
    AsmaulHusna(22, "الْخَافِضُ", "Al-Khafidz", "Maha Merendahkan"),
    AsmaulHusna(23, "الرَّافِعُ", "Ar-Rafi'", "Maha Meninggikan"),
    AsmaulHusna(24, "الْمُعِزُّ", "Al-Mu'izz", "Maha Memuliakan"),
    AsmaulHusna(25, "الْمُذِلُّ", "Al-Mudzill", "Maha Menghinakan"),
    AsmaulHusna(26, "السَّمِيعُ", "As-Sami'", "Maha Mendengar"),
    AsmaulHusna(27, "الْبَصِيرُ", "Al-Bashir", "Maha Melihat"),
    AsmaulHusna(28, "الْحَكَمُ", "Al-Hakam", "Maha Menetapkan Hukum"),
    AsmaulHusna(29, "الْعَدْلُ", "Al-Adl", "Maha Adil"),
    AsmaulHusna(30, "اللَّطِيفُ", "Al-Latif", "Maha Lembut"),
    AsmaulHusna(31, "الْخَبِيرُ", "Al-Khabir", "Maha Mengenal"),
    AsmaulHusna(32, "الْحَلِيمُ", "Al-Halim", "Maha Penyantun"),
    AsmaulHusna(33, "الْعَظِيمُ", "Al-Azhim", "Maha Agung"),
    AsmaulHusna(34, "الْغَفُورُ", "Al-Ghafur", "Maha Memberi Pengampunan"),
    AsmaulHusna(35, "الشَّكُورُ", "Asy-Syakur", "Maha Pembalas Budi"),
    AsmaulHusna(36, "الْعَلِيُّ", "Al-Ali", "Maha Tinggi"),
    AsmaulHusna(37, "الْكَبِيرُ", "Al-Kabir", "Maha Besar"),
    AsmaulHusna(38, "الْحَفِيظُ", "Al-Hafidz", "Maha Memelihara"),
    AsmaulHusna(39, "الْمُقِيتُ", "Al-Muqit", "Maha Pemberi Kecukupan"),
    AsmaulHusna(40, "الْحَسِيبُ", "Al-Hasib", "Maha Membuat Perhitungan"),
    AsmaulHusna(41, "الْجَلِيلُ", "Al-Jalil", "Maha Luhur"),
    AsmaulHusna(42, "الْكَرِيمُ", "Al-Karim", "Maha Pemurah"),
    AsmaulHusna(43, "الرَّقِيبُ", "Ar-Raqib", "Maha Mengawasi"),
    AsmaulHusna(44, "الْمُجِيبُ", "Al-Mujib", "Maha Mengabulkan"),
    AsmaulHusna(45, "الْوَاسِعُ", "Al-Wasi'", "Maha Luas"),
    AsmaulHusna(46, "الْحَكِيمُ", "Al-Hakim", "Maha Bijaksana"),
    AsmaulHusna(47, "الْوَدُودُ", "Al-Wadud", "Maha Mengasihi"),
    AsmaulHusna(48, "الْمَجِيدُ", "Al-Majid", "Maha Mulia"),
    AsmaulHusna(49, "الْبَاعِثُ", "Al-Ba'its", "Maha Membangkitkan"),
    AsmaulHusna(50, "الشَّهِيدُ", "Asy-Syahid", "Maha Menyaksikan"),
    AsmaulHusna(51, "الْحَقُّ", "Al-Haqq", "Maha Benar"),
    AsmaulHusna(52, "الْوَكِيلُ", "Al-Wakil", "Maha Memelihara"),
    AsmaulHusna(53, "الْقَوِيُّ", "Al-Qawiyy", "Maha Kuat"),
    AsmaulHusna(54, "الْمَتِينُ", "Al-Matin", "Maha Kokoh"),
    AsmaulHusna(55, "الْوَلِيُّ", "Al-Waliyy", "Maha Melindungi"),
    AsmaulHusna(56, "الْحَمِيدُ", "Al-Hamid", "Maha Terpuji"),
    AsmaulHusna(57, "الْمُحْصِي", "Al-Muhshi", "Maha Mengkalkulasi"),
    AsmaulHusna(58, "الْمُبْدِئُ", "Al-Mubdi'", "Maha Memulai"),
    AsmaulHusna(59, "الْمُعِيدُ", "Al-Mu'id", "Maha Mengembalikan Kehidupan"),
    AsmaulHusna(60, "الْمُحْيِي", "Al-Muhyi", "Maha Menghidupkan"),
    AsmaulHusna(61, "الْمُمِيتُ", "Al-Mumit", "Maha Mematikan"),
    AsmaulHusna(62, "الْحَيُّ", "Al-Hayy", "Maha Hidup"),
    AsmaulHusna(63, "الْقَيُّومُ", "Al-Qayyum", "Maha Mandiri"),
    AsmaulHusna(64, "الْوَاجِدُ", "Al-Wajid", "Maha Penemu"),
    AsmaulHusna(65, "الْمَاجِدُ", "Al-Majid", "Maha Mulia"),
    AsmaulHusna(66, "الْوَاحِدُ", "Al-Wahid", "Maha Tunggal"),
    AsmaulHusna(67, "الاَحَدُ", "Al-Ahad", "Maha Esa"),
    AsmaulHusna(68, "الصَّمَدُ", "Ash-Shamad", "Maha Dibutuhkan"),
    AsmaulHusna(69, "الْقَادِرُ", "Al-Qadir", "Maha Menentukan"),
    AsmaulHusna(70, "الْمُقْتَدِرُ", "Al-Muqtadir", "Maha Berkuasa"),
    AsmaulHusna(71, "الْمُقَدِّمُ", "Al-Muqaddim", "Maha Mendahulukan"),
    AsmaulHusna(72, "الْمُؤَخِّرُ", "Al-Mu'akkhir", "Maha Mengakhirkan"),
    AsmaulHusna(73, "الاَوَّلُ", "Al-Awwal", "Maha Awal"),
    AsmaulHusna(74, "الاَخِرُ", "Al-Akhir", "Maha Akhir"),
    AsmaulHusna(75, "الظَّاهِرُ", "Adz-Dzahir", "Maha Nyata"),
    AsmaulHusna(76, "الْبَاطِنُ", "Al-Bathin", "Maha Tersembunyi"),
    AsmaulHusna(77, "الْوَالِي", "Al-Wali", "Maha Memerintah"),
    AsmaulHusna(78, "الْمُتَعَالِي", "Al-Muta'ali", "Maha Tinggi"),
    AsmaulHusna(79, "الْبَرُّ", "Al-Barr", "Maha Penderma"),
    AsmaulHusna(80, "التَّوَّابُ", "At-Tawwab", "Maha Penerima Tobat"),
    AsmaulHusna(81, "الْمُنْتَقِمُ", "Al-Muntaqim", "Maha Pemberi Balasan"),
    AsmaulHusna(82, "العَفُوُّ", "Al-Afuww", "Maha Pemaaf"),
    AsmaulHusna(83, "الرَّؤُوفُ", "Ar-Ra'uf", "Maha Pengasuh"),
    AsmaulHusna(84, "مَالِكُ الْمُلْكِ", "Malikul Mulk", "Maha Penguasa Kerajaan"),
    AsmaulHusna(85, "ذُوالْجَلاَلِ وَالإِكْرَامِ", "Dzul Jalaali Wal Ikraam", "Maha Memiliki Kebesaran dan Kemuliaan"),
    AsmaulHusna(86, "الْمُقْسِطُ", "Al-Muqsit", "Maha Pemberi Keadilan"),
    AsmaulHusna(87, "الْجَامِعُ", "Al-Jami'", "Maha Mengumpulkan"),
    AsmaulHusna(88, "الْغَنِيُّ", "Al-Ghaniyy", "Maha Kaya"),
    AsmaulHusna(89, "الْمُغْنِي", "Al-Mughni", "Maha Pemberi Kekayaan"),
    AsmaulHusna(90, "الْمَانِعُ", "Al-Mani'", "Maha Mencegah"),
    AsmaulHusna(91, "الضَّارُّ", "Ad-Dharr", "Maha Penimpa Kemudharatan"),
    AsmaulHusna(92, "النَّافِعُ", "An-Nafi'", "Maha Pemberi Manfaat"),
    AsmaulHusna(93, "النُّورُ", "An-Nur", "Maha Bercahaya"),
    AsmaulHusna(94, "الْهَادِي", "Al-Hadi", "Maha Pemberi Petunjuk"),
    AsmaulHusna(95, "الْبَدِيعُ", "Al-Badi'", "Maha Pencipta Tiada Bandingnya"),
    AsmaulHusna(96, "الْبَاقِي", "Al-Baqi", "Maha Kekal"),
    AsmaulHusna(97, "الْوَارِثُ", "Al-Warits", "Maha Pewaris"),
    AsmaulHusna(98, "الرَّشِيدُ", "Ar-Rasyid", "Maha Pandai"),
    AsmaulHusna(99, "الصَّبُورُ", "Ash-Shabur", "Maha Sabar")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsmaulHusnaScreen(navController: NavController) {
    Scaffold(
        containerColor = Color(0xFF121212)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Header Baru yang Konsisten
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.asmaulhusna_sholat),
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
                        text = "Asmaul Husna",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "99 Nama Allah yang Indah",
                        color = Color.White.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(daftarAsmaulHusna) { asma ->
                    AsmaulHusnaCard(asma)
                }
            }
        }
    }
}

@Composable
fun AsmaulHusnaCard(asma: AsmaulHusna) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = asma.nomor.toString(),
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
            Text(
                text = asma.arab,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00C853),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = asma.latin,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.White
            )
            Text(
                text = asma.arti,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = Color.Gray,
                lineHeight = 16.sp
            )
        }
    }
}
