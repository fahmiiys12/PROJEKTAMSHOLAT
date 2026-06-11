package com.example.muslimone

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.muslimone.ui.DetailHeader

data class Ayat(val nomor: Int, val arab: String, val latin: String, val arti: String)

data class Surah(val nomor: Int, val nama: String, val arab: String, val arti: String, val jumlahAyat: Int, val ayat: List<Ayat>)

val daftarSurah = listOf(
    Surah(1, "Al-Fatihah", "الفاتحة", "Pembukaan", 7, listOf(
        Ayat(1, "بِسْمِ اللَّهِ الرَّحْمَنِ الرَّحِيمِ", "Bismillāhir-raḥmānir-raḥīm", "Dengan nama Allah Yang Maha Pengasih, Maha Penyayang."),
        Ayat(2, "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ", "Al-ḥamdu lillāhi rabbil-'ālamīn", "Segala puji bagi Allah, Tuhan seluruh alam,"),
        Ayat(3, "الرَّحْمَنِ الرَّحِيمِ", "Ar-raḥmānir-raḥīm", "Yang Maha Pengasih, Maha Penyayang,"),
        Ayat(4, "مَالِكِ يَوْمِ الدِّينِ", "Māliki yaumid-dīn", "Pemilik hari pembalasan."),
        Ayat(5, "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ", "Iyyāka na'budu wa iyyāka nasta'īn", "Hanya kepada Engkaulah kami menyembah dan hanya kepada Engkaulah kami mohon pertolongan."),
        Ayat(6, "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ", "Ihdinaṣ-ṣirāṭal-mustaqīm", "Tunjukilah kami jalan yang lurus,"),
        Ayat(7, "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ", "Ṣirāṭallażīna an'amta 'alaihim gairil-magḍūbi 'alaihim wa laḍ-ḍāllīn", "(yaitu) jalan orang-orang yang telah Engkau beri nikmat kepadanya; bukan (jalan) mereka yang dimurkai, dan bukan (pula jalan) mereka yang sesat.")
    )),
    Surah(2, "Al-Baqarah", "البقرة", "Sapi Betina", 286, listOf(
        Ayat(1, "الٓمٓ", "Alif-Lām-Mīm", "Alif Lam Mim"),
        Ayat(2, "ذَٰلِكَ ٱلْكِتَـٰبُ لَا رَيْبَ ۛ فِيهِ ۛ هُدًۭى لِّلْمُتَّقِينَ", "Żālikal-kitābu lā raiba fīh(i), hudan lil-muttaqīn(a)", "Kitab (Al-Qur'an) ini tidak ada keraguan padanya; petunjuk bagi mereka yang bertakwa,")
    )),
    Surah(112, "Al-Ikhlas", "الإخلاص", "Memurnikan Keesaan Allah", 4, listOf(
        Ayat(1, "قُلْ هُوَ اللَّهُ أَحَدٌ", "Qul huwallāhu aḥad", "Katakanlah (Muhammad), \"Dialah Allah, Yang Maha Esa."),
        Ayat(2, "اللَّهُ الصَّمَدُ", "Allāhuṣ-ṣamad", "Allah tempat meminta segala sesuatu."),
        Ayat(3, "لَمْ يَلِدْ وَلَمْ يُولَدْ", "Lam yalid wa lam yūlad", "(Allah) tidak beranak dan tidak pula diperanakkan,"),
        Ayat(4, "وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ", "Wa lam yakul lahū kufuwan aḥad", "dan tidak ada sesuatu yang setara dengan Dia.\"")
    )),
    Surah(113, "Al-Falaq", "الفلق", "Waktu Subuh", 5, listOf(
        Ayat(1, "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ", "Qul a'ūżu birabbil-falaq", "Katakanlah, \"Aku berlindung kepada Tuhan yang menguasai subuh (fajar),"),
        Ayat(2, "مِن شَرِّ مَا خَلَقَ", "Min syarri mā khalaq", "dari kejahatan (makhluk) yang Dia ciptakan,"),
        Ayat(3, "وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ", "Wa min syarri gāsiqin iżā waqab", "dan dari kejahatan malam apabila telah gelap gulita,"),
        Ayat(4, "وَمِن شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ", "Wa min syarrin-naffāṡāti fil-'uqad", "dan dari kejahatan (perempuan-perempuan) penyihir yang meniup pada buhul-buhul (talinya),"),
        Ayat(5, "وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ", "Wa min syarri ḥāsidin iżā ḥasad", "dan dari kejahatan orang yang dengki apabila dia dengki.\"")
    )),
    Surah(114, "An-Nas", "الناس", "Manusia", 6, listOf(
        Ayat(1, "قُلْ أَعُوذُ بِرَبِّ النَّاسِ", "Qul a'ūżu birabbin-nās", "Katakanlah, \"Aku berlindung kepada Tuhannya manusia,"),
        Ayat(2, "مَلِكِ النَّاسِ", "Malikin-nās", "Raja manusia,"),
        Ayat(3, "إِلَٰهِ النَّاسِ", "Ilāhin-nās", "Sembahan manusia,"),
        Ayat(4, "مِن شَرِّ الْوَسْوَاسِ الْخَنَّاسِ", "Min syarril-waswāsil-khannās", "dari kejahatan (bisikan) setan yang bersembunyi,"),
        Ayat(5, "الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ", "Allażī yuwaswisu fī ṣudūrin-nās", "yang membisikkan (kejahatan) ke dalam dada manusia,"),
        Ayat(6, "مِنَ الْجِنَّةِ وَالنَّاسِ", "Minal-jinnati wan-nās", "dari (golongan) jin dan manusia.\"")
    )),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredSurah = remember(searchQuery) {
        if (searchQuery.isEmpty()) {
            daftarSurah
        } else {
            daftarSurah.filter { 
                it.nama.contains(searchQuery, ignoreCase = true) || 
                it.arti.contains(searchQuery, ignoreCase = true) ||
                it.nomor.toString() == searchQuery
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
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
                    imageSource = R.drawable.alquran_sholat,
                    onBack = { navController.popBackStack() }
                )
            }

            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    placeholder = { Text("Cari Surah (contoh: Al-Fatihah atau 112)", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Hapus", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }

            items(filteredSurah) { surah ->
                SurahItem(surah, navController)
            }
            
            if (filteredSurah.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("Surah tidak ditemukan", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}


@Composable
fun SurahItem(surah: Surah, navController: NavController) {
    ListItem(
        headlineContent = { 
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(surah.nama, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Text(
                    text = surah.arab,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        supportingContent = { Text("${surah.arti} • ${surah.jumlahAyat} Ayat", color = MaterialTheme.colorScheme.onSurfaceVariant) },
        leadingContent = {
            Surface(
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = surah.nomor.toString(),
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        modifier = Modifier.clickable { navController.navigate("surah_detail/${surah.nomor}") }
    )
    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.12f))
}

@Composable
fun SurahDetailScreen(surahId: Int, navController: NavController) {
    val surah = daftarSurah.find { it.nomor == surahId } ?: return
    
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
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
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFF2E7D32), Color(0xFF1B5E20))
                            )
                        )
                        .padding(24.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Text(surah.arab, style = MaterialTheme.typography.headlineMedium, color = Color.White)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(surah.nama, style = MaterialTheme.typography.headlineLarge, color = Color.White, fontWeight = FontWeight.Bold)
                        Text(surah.arti, style = MaterialTheme.typography.bodyLarge, color = Color.White.copy(alpha = 0.8f))
                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = Color.White.copy(alpha = 0.5f), modifier = Modifier.width(100.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("${surah.jumlahAyat} AYAT", style = MaterialTheme.typography.labelLarge, color = Color.White)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("بِسْمِ اللَّهِ الرَّحْمَنِ الرَّحِيمِ", style = MaterialTheme.typography.headlineMedium, color = Color.White)
                    }
                }
            }

            items(surah.ayat) { ayat ->
                AyatItem(ayat)
            }
        }
    }
}

@Composable
fun AyatItem(ayat: Ayat) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(ayat.nomor.toString(), color = MaterialTheme.colorScheme.onPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = ayat.arab,
            style = MaterialTheme.typography.headlineMedium.copy(lineHeight = 48.sp),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = androidx.compose.ui.text.style.TextAlign.Right,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = ayat.latin,
            style = MaterialTheme.typography.bodyMedium.copy(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = ayat.arti,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.12f))
    }
}
