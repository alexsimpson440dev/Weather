package com.ics342.weather.interfaces

import com.ics342.weather.domains.CurrentConditions
import com.ics342.weather.domains.Forecast
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("weather")
    suspend fun getCurrentConditions(
        @Query("zip") zip: String,
        @Query("units") units: String = "imperial",
        @Query("appid") appId: String = "a5be1cee30b6093e05faa76a1d3cb9be"
    ): CurrentConditions

    @GET("forecast/daily")
    suspend fun getForecast(
        @Query("zip") zip: String,
        @Query("units") units: String = "imperial",
        @Query("cnt") count: Int = 16,
        @Query("appid") appId: String = "a5be1cee30b6093e05faa76a1d3cb9be"
    ): Forecast
}
