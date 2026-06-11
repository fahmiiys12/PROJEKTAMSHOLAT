package com.example.muslimone

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.muslimone.ui.DetailHeader
import kotlin.math.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KiblatScreen(navController: NavController) {
    val context = LocalContext.current
    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    
    var azimuth by remember { mutableStateOf(0f) }
    var qiblaDirection by remember { mutableStateOf(295f) }
    var currentAddress by remember { mutableStateOf("Mendeteksi lokasi...") }
    val accelerometerReading = remember { FloatArray(3) }
    val magnetometerReading = remember { FloatArray(3) }
    val rotationMatrix = remember { FloatArray(9) }
    val orientationAngles = remember { FloatArray(3) }

    val sensorListener = remember {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event == null) return
                if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                    System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.size)
                } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                    System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
                }

                SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading)
                SensorManager.getOrientation(rotationMatrix, orientationAngles)
                azimuth = Math.toDegrees(orientationAngles[0].toDouble()).toFloat()
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }

    LaunchedEffect(Unit) {
        val location = LocationHelper.getCurrentLocation(context)
        if (location != null) {
            currentAddress = LocationHelper.getCurrentLocationName(context)
            qiblaDirection = calculateQiblaDirection(location.latitude, location.longitude)
        }
    }

    DisposableEffect(Unit) {
        val accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val mag = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        
        sensorManager.registerListener(sensorListener, accel, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(sensorListener, mag, SensorManager.SENSOR_DELAY_UI)
        
        onDispose {
            sensorManager.unregisterListener(sensorListener)
        }
    }

    val animatedAzimuth by animateFloatAsState(targetValue = -azimuth)

    Scaffold(
        containerColor = Color(0xFF121212)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DetailHeader(
                title = "Arah Kiblat",
                subtitle = currentAddress,
                imageSource = R.drawable.kiblat_sholat,
                onBack = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(40.dp))

            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(300.dp)) {
                Surface(
                    modifier = Modifier.size(280.dp),
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.05f),
                    border = androidx.compose.foundation.BorderStroke(2.dp, Color.White.copy(alpha = 0.1f))
                ) {
                    Box(modifier = Modifier.fillMaxSize().rotate(animatedAzimuth)) {
                        Text("N", Modifier.align(Alignment.TopCenter).padding(8.dp), Color.Red, fontWeight = FontWeight.Bold)
                        Text("S", Modifier.align(Alignment.BottomCenter).padding(8.dp), Color.White)
                        Text("E", Modifier.align(Alignment.CenterEnd).padding(8.dp), Color.White)
                        Text("W", Modifier.align(Alignment.CenterStart).padding(8.dp), Color.White)
                    }
                }
                Icon(
                    painter = painterResource(id = android.R.drawable.btn_star_big_on),
                    contentDescription = "Ka'bah",
                    modifier = Modifier
                        .size(40.dp)
                        .rotate(animatedAzimuth + qiblaDirection)
                        .offset(y = (-110).dp),
                    tint = Color(0xFFFFC107)
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .rotate(animatedAzimuth + qiblaDirection),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .height(120.dp)
                            .width(4.dp)
                            .offset(y = (-60).dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color(0xFF00C853))
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E242C)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.padding(horizontal = 24.dp).fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Sudut Kiblat: ${qiblaDirection.toInt()}°",
                        color = Color(0xFF00C853),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Posisikan jarum hijau tepat ke atas untuk menghadap Kiblat.",
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

fun calculateQiblaDirection(lat: Double, lon: Double): Float {
    val kaabaLat = Math.toRadians(21.4225)
    val kaabaLon = Math.toRadians(39.8262)
    val userLat = Math.toRadians(lat)
    val userLon = Math.toRadians(lon)

    val deltaLon = kaabaLon - userLon

    val y = sin(deltaLon)
    val x = cos(userLat) * tan(kaabaLat) - sin(userLat) * cos(deltaLon)
    
    var qibla = Math.toDegrees(atan2(y, x)).toFloat()
    if (qibla < 0) qibla += 360f
    return qibla
}
