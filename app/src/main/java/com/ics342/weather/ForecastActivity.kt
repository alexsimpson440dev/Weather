package com.ics342.weather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ics342.weather.domains.Forecast
import com.ics342.weather.domains.DayForecast
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ForecastActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private var adapterData = mutableListOf<DayForecast>()
    private lateinit var adapter: ForecastAdapter
    private lateinit var api: Api

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        recyclerView = findViewById(R.id.recycler_view)
        adapter = ForecastAdapter(adapterData)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        api = retrofit.create(Api::class.java)
    }

    override fun onResume() {
        super.onResume()
        val call: Call<Forecast> = api.getForecast("55304")
        call.enqueue(object : Callback<Forecast> {
            override fun onResponse(
                call: Call<Forecast>,
                response: Response<Forecast>
            ) {
                val forecasts = response.body()
                forecasts?.let {
                    bindData(it)
                }
            }

            override fun onFailure(call: Call<Forecast>, t: Throwable) {
                println("An error occurred due to: $t")
            }
        })
    }

    private fun bindData(forecast: Forecast) {
        for (i in 0 until forecast.list.count()) {
            adapter.appendData(forecast.list[i])
        }
    }
}
