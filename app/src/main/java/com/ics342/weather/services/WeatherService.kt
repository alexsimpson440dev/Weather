package com.ics342.weather.services

import android.app.*
import android.app.PendingIntent.getActivity
import android.content.Intent
import android.os.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.ics342.weather.R
import com.ics342.weather.fragments.SearchFragment

@RequiresApi(Build.VERSION_CODES.O)
class WeatherService : Service() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, getString(R.string.starting_service), Toast.LENGTH_SHORT).show()
        val temperature = intent?.getStringExtra("temperature")
        val notification: Notification = getNotification(temperature.orEmpty())

        startForeground(NOTIFICATION_ID, notification)

        return super.onStartCommand(intent, flags, startId)
    }

    private fun getNotification(temperature: String = getString(R.string.double_dash)): Notification {
        val pendingIntent: PendingIntent =
            getActivity(
                this,
                0,
                Intent(this, SearchFragment::class.java),
                PendingIntent.FLAG_IMMUTABLE
            )

        return Notification.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.current_temperature))
            .setContentText(temperature + getString(R.string.degree_symbol))
            .setSmallIcon(R.drawable.sun_small)
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val weatherNotificationChannel = NotificationChannel(
                CHANNEL_ID,
                "Weather Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
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
