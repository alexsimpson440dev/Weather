package com.ics342.weather.domains

data class Forecast(
    val list: List<DayForecast>
)

data class DayForecast(
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: ForecastTemp,
    val pressure: Float,
    val humidity: Int,
    val weather: List<Weather>
)
