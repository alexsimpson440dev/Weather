package com.ics342.weather.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ics342.weather.interfaces.Api
import com.ics342.weather.domains.CurrentConditions
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

class MainViewModel @Inject constructor(private val service: Api): ViewModel() {

    val currentConditions: MutableLiveData<CurrentConditions> = MutableLiveData()

    fun loadData() = runBlocking {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        service = retrofit.create(Api::class.java)

        launch { service.getCurrentConditions("55304") }
        call.enqueue(object : Callback<CurrentConditions> {
            override fun onResponse(
                call: Call<CurrentConditions>,
                response: Response<CurrentConditions>
            ) {
                response.body()?.let {
                    currentConditions.value = it
                }
            }

            override fun onFailure(call: Call<CurrentConditions>, t: Throwable) {
                println("An error occurred due to: $t")
            }
        })
    }
}