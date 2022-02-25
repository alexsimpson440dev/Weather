package com.ics342.weather

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
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

    private lateinit var locationName: TextView
    private lateinit var currentTemp: TextView
    private lateinit var conditionIcon: ImageView
    private lateinit var feelsLike: TextView
    private lateinit var lowTemp: TextView
    private lateinit var highTemp: TextView
    private lateinit var humidity: TextView
    private lateinit var pressure: TextView

    private lateinit var forecastButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationName = findViewById(R.id.location_name)
        currentTemp = findViewById(R.id.temperature)
        conditionIcon = findViewById(R.id.condition_icon)
        feelsLike = findViewById(R.id.feels_like)
        lowTemp = findViewById(R.id.low)
        highTemp = findViewById(R.id.high)
        humidity = findViewById(R.id.humidity)
        pressure = findViewById(R.id.pressure)

        forecastButton = findViewById(R.id.forecastButton)
        forecastButton.setOnClickListener {
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
        locationName.text = currentConditions.name
        currentTemp.text = getString(R.string.temperature, currentConditions.main.temp)
        feelsLike.text = getString(R.string.feels_like, currentConditions.main.feelsLike)
        lowTemp.text = getString(R.string.low, currentConditions.main.tempMin)
        highTemp.text = getString(R.string.high, currentConditions.main.tempMax)
        humidity.text = getString(R.string.humidity, currentConditions.main.humidity)
        pressure.text = getString(R.string.pressure, currentConditions.main.pressure)

        val iconName = currentConditions.weather.firstOrNull()?.icon
        val iconUrl = "https://openweathermap.org/img/wn/$iconName@2x.png"
        Glide.with(this)
            .load(iconUrl)
            .into(conditionIcon)
    }
}
