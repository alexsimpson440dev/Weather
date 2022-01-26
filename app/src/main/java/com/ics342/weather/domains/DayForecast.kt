package com.ics342.weather.domains

data class DayForecast(
    val date: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: ForecastTemp,
    val pressure: Float,
    val humidity: Int
)
