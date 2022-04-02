package com.ics342.weather.domains

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class CurrentConditions(
    val name: String,
    val main: Main,
    val weather: List<Weather>
) : Parcelable

@Parcelize
data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
) : Parcelable

@Parcelize
data class Main(
    val temp: Float,
    @Json(name = "feels_like") val feelsLike: Float,
    @Json(name = "temp_min") val tempMin: Float,
    @Json(name = "temp_max") val tempMax: Float,
    val pressure: Int,
    val humidity: Int
) : Parcelable
