package com.example.muslimone.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.muslimone.R

class AdzanReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val sholatName = intent.getStringExtra("SHOLAT_NAME") ?: "Waktu Sholat"
        showNotification(context, sholatName)
    }

    private fun showNotification(context: Context, sholatName: String) {
        val channelId = "adzan_channel"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Notifikasi Adzan",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Mengingatkan waktu sholat"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("Waktunya Sholat $sholatName")
            .setContentText("Marilah menuju kemenangan, sholat $sholatName telah tiba.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(sholatName.hashCode(), notification)
    }
}
