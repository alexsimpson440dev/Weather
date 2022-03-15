package com.ics342.weather.domains

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Forecast(
    val list: List<DayForecast>
) : Parcelable

@Parcelize
data class DayForecast(
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: ForecastTemp,
    val pressure: Float,
    val humidity: Int,
    val weather: List<Weather>
) : Parcelable
