package com.ics342.weather.domains

import com.squareup.moshi.Json

data class CurrentConditions(
    val name: String,
    val main: Main,
    val weather: List<Weather>
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Main(
    val temp: Float,
    @Json(name = "feels_like") val feelsLike: Float,
    @Json(name = "temp_min") val tempMin: Float,
    @Json(name = "temp_max") val tempMax: Float,
    val pressure: Int,
    val humidity: Int
)


