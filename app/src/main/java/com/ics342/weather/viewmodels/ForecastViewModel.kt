package com.ics342.weather.viewmodels

import androidx.lifecycle.MutableLiveData
import com.ics342.weather.interfaces.Api
import com.ics342.weather.domains.Forecast
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ForecastViewModel {

    val forecast: MutableLiveData<Forecast> = MutableLiveData()

    fun loadData() {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        val service = retrofit.create(Api::class.java)
        val call: Call<Forecast> = service.getForecast("55304")
        call.enqueue(object : Callback<Forecast> {
            override fun onResponse(
                call: Call<Forecast>,
                response: Response<Forecast>
            ) {

                response.body()?.let {
                    forecast.value = it
                }
            }

            override fun onFailure(call: Call<Forecast>, t: Throwable) {
                println("An error occurred due to: $t")
            }
        })
    }
}