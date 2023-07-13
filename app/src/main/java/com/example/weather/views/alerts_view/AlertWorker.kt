package com.example.weather.views.alerts_view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.weather.R
import com.example.weather.data_source.alert_repo.AlertRepo
import com.example.weather.data_source.alert_repo.alert_local.AlertLocalSource
import com.example.weather.data_source.alert_repo.alert_pojo.Alert
import com.example.weather.data_source.alert_repo.alert_pojo.AlertResponse
import com.example.weather.data_source.alert_repo.alert_pojo.SavedAlert
import com.example.weather.data_source.alert_repo.alert_remote.AlertApiClient
import com.example.weather.main_activity.MainActivity
import com.example.weather.system.companion.MyCompanion
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest

class AlertWorker(private val ctx: Context, private val workerParameters: WorkerParameters) :
    CoroutineWorker(ctx, workerParameters) {
    @RequiresApi(Build.VERSION_CODES.M)
    private val notificationManager =
        ctx.getSystemService(NotificationManager::class.java)
    private val repo =
        AlertRepo.getInstance(AlertLocalSource.getInstance(ctx), AlertApiClient.getInstance())
    val sound: MediaPlayer = MediaPlayer.create(ctx, R.raw.alarm)
    //sound.start()


    @RequiresApi(Build.VERSION_CODES.M)
    override suspend fun doWork(): Result {
        val longitude = inputData.getDouble(MyCompanion.LONGITUDE, 0.0)
        val latitude = inputData.getDouble(MyCompanion.LATITUDE, 0.0)
        val notificationOption = inputData.getString(MyCompanion.NOTIFI_OPTION)
        val startTime = inputData.getLong(MyCompanion.START_TIME, 0)
        val endTime = inputData.getLong(MyCompanion.END_TIME, 0)
        var alertResponse: AlertResponse? = null
        withContext(Dispatchers.IO) {

            repo.getRemoteAlerts(latitude, longitude).collectLatest {
                if (it.alerts != null) {
                    alertResponse = it
                }
            }
        }
        var alerts = alertResponse!!.alerts
        if (!(alerts.isNullOrEmpty())) {
            for (alert in alerts) {
                repo.insertAlert(SavedAlert(workerParameters.id, "Test", 0.0, 0.0, alert.start))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channel = NotificationChannel(
                        MyCompanion.APP_CHANEL,
                        MyCompanion.APP_CHANEL,
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                    notificationManager.createNotificationChannel(channel)
                }
                val intent = Intent(ctx, MainActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                val pendingIntent =
                    PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_IMMUTABLE)
                val builder = NotificationCompat.Builder(ctx, MyCompanion.APP_CHANEL)
                    .setSmallIcon(R.drawable.weather_logo)
                    .setContentTitle(MyCompanion.APP_CHANEL)
                    .setContentText(alert.description)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                notificationManager.notify(0, builder.build())
                sound.start()
            }
            return Result.success()
        } else {
            return Result.failure()
        }

    }
}
