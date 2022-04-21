package com.ics342.weather.services

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.ics342.weather.R
import com.ics342.weather.fragments.SearchFragment

class WeatherService : Service() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show()

        val pendingIntent: PendingIntent =
            Intent(this, SearchFragment::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
            }

        val notification: Notification = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle("f") // this should be weather stuff
            .setContentText("ff")
            .setSmallIcon(R.drawable.sun_small)
            .setContentIntent(pendingIntent)
            .setTicker("fff")
            .build()

        startForeground(NOTIFICATION_ID, notification)
        return super.onStartCommand(intent, flags, startId)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val weatherNotificationChannel = NotificationChannel(CHANNEL_ID, "Weather Channel", NotificationManager.IMPORTANCE_DEFAULT)
            weatherNotificationChannel.setSound(null, null)
            val manager = getSystemService(NotificationManager::class.java)

            manager.createNotificationChannel(weatherNotificationChannel)
        }
    }

    companion object {
        const val NOTIFICATION_ID = 125
        const val CHANNEL_ID = "weather_notification_channel"
    }
}