package com.ics342.weather

import com.ics342.weather.domains.CurrentConditions
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("weather")
    fun getCurrentConditions(
        @Query("zip") zip: String,
        @Query("units") units: String = "imperial",
        @Query("appid") appId: String = "a5be1cee30b6093e05faa76a1d3cb9be"
    ): Call<CurrentConditions>
}
