package com.example.muslimone.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.muslimone.LocationHelper
import com.example.muslimone.data.api.ApiService
import com.example.muslimone.data.repository.SholatRepository
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class AdzanWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val repository = SholatRepository(ApiService.create())
        
        try {
            val location = LocationHelper.getCurrentLocation(applicationContext)
            if (location == null) return Result.retry()

            val result = repository.getPrayerTimesByCoords(location.latitude, location.longitude)
            
            result.onSuccess { sholatList ->
                sholatList.forEach { sholat ->
                    scheduleAlarm(sholat.name, sholat.time)
                }
            }
            return Result.success()
        } catch (e: Exception) {
            Log.e("AdzanWorker", "Error in doWork: ${e.message}")
            return Result.failure()
        }
    }

    private fun scheduleAlarm(name: String, time: String) {
        val alarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(applicationContext, AdzanReceiver::class.java).apply {
            putExtra("SHOLAT_NAME", name)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            name.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val prayerTimeDate = sdf.parse(time) ?: return
        
        val calendar = Calendar.getInstance().apply {
            val now = Calendar.getInstance()
            
            val prayerCal = Calendar.getInstance()
            prayerCal.time = prayerTimeDate
            
            set(Calendar.HOUR_OF_DAY, prayerCal.get(Calendar.HOUR_OF_DAY))
            set(Calendar.MINUTE, prayerCal.get(Calendar.MINUTE))
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            
            if (before(now)) {
                add(Calendar.DATE, 1)
            }
        }

        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
            Log.d("AdzanWorker", "Scheduled $name at ${calendar.time}")
        } catch (e: SecurityException) {
            Log.e("AdzanWorker", "SecurityException scheduling alarm: ${e.message}")
        }
    }

    companion object {
        fun setupPeriodicWork(context: Context) {
            val workRequest = PeriodicWorkRequestBuilder<AdzanWorker>(1, TimeUnit.DAYS)
                .addTag("adzan_worker")
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "adzan_periodic_work",
                androidx.work.ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
        }
    }
}
