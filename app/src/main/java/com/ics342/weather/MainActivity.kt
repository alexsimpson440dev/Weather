package com.ics342.weather

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.ics342.weather.databinding.ActivityMainBinding
import com.ics342.weather.domains.CurrentConditions
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var api: Api
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.forecastButton.setOnClickListener {
            startActivity(Intent(this, ForecastActivity::class.java))
        }

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
        val call: Call<CurrentConditions> = api.getCurrentConditions("55304")
        call.enqueue(object : Callback<CurrentConditions> {
            override fun onResponse(
                call: Call<CurrentConditions>,
                response: Response<CurrentConditions>
            ) {
                val currentConditions = response.body()
                currentConditions?.let {
                    bindData(it)
                }
            }

            override fun onFailure(call: Call<CurrentConditions>, t: Throwable) {
                println("An error occurred due to: $t")
            }
        })
    }

    private fun bindData(currentConditions: CurrentConditions) {
        binding.locationName.text = currentConditions.name
        binding.temperature.text = getString(R.string.temperature, currentConditions.main.temp)
        binding.feelsLike.text = getString(R.string.feels_like, currentConditions.main.feelsLike)
        binding.low.text = getString(R.string.low, currentConditions.main.tempMin)
        binding.high.text = getString(R.string.high, currentConditions.main.tempMax)
        binding.humidity.text = getString(R.string.humidity, currentConditions.main.humidity)
        binding.pressure.text = getString(R.string.pressure, currentConditions.main.pressure)

        val iconName = currentConditions.weather.firstOrNull()?.icon
        val iconUrl = "https://openweathermap.org/img/wn/$iconName@2x.png"
        Glide.with(this)
            .load(iconUrl)
            .into(binding.conditionIcon)
    }
}
