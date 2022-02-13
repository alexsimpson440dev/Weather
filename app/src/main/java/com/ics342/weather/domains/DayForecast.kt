package com.ics342.weather.domains

data class DayForecast(
    val date: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: ForecastTemp,
    val pressure: Float = 1f,
    val humidity: Int = 0
)
