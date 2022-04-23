package com.ics342.weather.services

import android.Manifest
import android.app.*
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.ics342.weather.R
import com.ics342.weather.fragments.SearchFragment
import okhttp3.internal.notify

@RequiresApi(Build.VERSION_CODES.O)
class WeatherService : Service() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {}
        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        locationRequest.interval = 0 // todo: set to 30 minutes
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show()

        val notification: Notification = getNotification("temp")

        getCurrentLocation()

        startForeground(NOTIFICATION_ID, notification)

        return super.onStartCommand(intent, flags, startId)
    }

    private fun getNotification(temperature: String): Notification {
        val pendingIntent: PendingIntent =
            getActivity(this, 0, Intent(this, SearchFragment::class.java), PendingIntent.FLAG_IMMUTABLE)

        return Notification.Builder(this, CHANNEL_ID)
            .setContentTitle("Some Title") // this should be weather stuff
            .setContentText(temperature)
            .setSmallIcon(R.drawable.sun_small)
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val weatherNotificationChannel = NotificationChannel(CHANNEL_ID, "Weather Channel", NotificationManager.IMPORTANCE_DEFAULT)
            weatherNotificationChannel.setSound(null, null)
            val manager = getSystemService(NotificationManager::class.java)

            manager.createNotificationChannel(weatherNotificationChannel)
        }
    }

    private fun getCurrentLocation() {
        if (checkPermissions()) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(this, "Failed to get location permissions", Toast.LENGTH_SHORT).show()
                return
            }
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location == null) {
                    updateLocation()
                } else {
                    onLocationObtained(location)
                }
            }
        } else {
            Toast.makeText(this, "Location permissions not granted", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun checkPermissions() = ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED


    private fun onLocationObtained(location: Location) {
        val notification = getNotification(location.toString())

        startForeground(NOTIFICATION_ID, notification)
    }

    companion object {
        const val NOTIFICATION_ID = 125
        const val CHANNEL_ID = "weather_notification_channel"
    }
}