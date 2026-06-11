package com.example.muslimone

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.tasks.await
import java.util.*

object LocationHelper {
    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(context: Context): Location? {
        return try {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            val location = fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).await()
            location ?: fusedLocationClient.lastLocation.await()
        } catch (e: Exception) {
            null
        }
    }

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocationName(context: Context): String {
        return try {
            val location = getCurrentLocation(context)
            if (location != null) {
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0]
                    address.subLocality ?: address.locality ?: address.subAdminArea ?: "Lokasi Tidak Dikenal"
                } else {
                    "Koordinat: ${location.latitude}, ${location.longitude}"
                }
            } else {
                "Gagal mendapatkan lokasi"
            }
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
}
