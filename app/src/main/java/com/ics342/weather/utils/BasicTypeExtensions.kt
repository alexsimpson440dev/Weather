package com.ics342.weather.utils

import android.annotation.SuppressLint
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@SuppressLint("NewApi")
fun Long.getDateTime(): LocalDateTime {
    return LocalDateTime.ofInstant(Instant.ofEpochSecond(this), ZoneId.systemDefault())
}
