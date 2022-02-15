package com.ics342.weather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ics342.weather.domains.DailyForecast16Days
import com.ics342.weather.domains.DayForecast
import com.ics342.weather.domains.ForecastTemp
import com.ics342.weather.domains.Weather
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
    private lateinit var api: Api

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.adapter = ForecastAdapter(adapterData, this)
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
        val call: Call<DailyForecast16Days> = api.get16DayForecast("55304")
        call.enqueue(object : Callback<DailyForecast16Days> {
            override fun onResponse(
                call: Call<DailyForecast16Days>,
                response: Response<DailyForecast16Days>
            ) {
                val forecasts = response.body()
                forecasts?.let {
                    bindData(it)
                }
            }

            override fun onFailure(call: Call<DailyForecast16Days>, t: Throwable) {
                println("An error occurred due to: $t")
            }
        })
    }

    private fun bindData(dailyForecast16Days: DailyForecast16Days) {
        dailyForecast16Days.list.forEach {
            adapterData.add(it)
            recyclerView.adapter?.notifyDataSetChanged()
        }

//        adapterData = dayForecasts
//        val iconName = dayForecast.weather.firstOrNull()?.icon
//        val iconUrl = "https://openweathermap.org/img/wn/$iconName@2x.png"
//        Glide.with(this)
//            .load(iconUrl)
//            .into(conditionIcon)
    }
}
