package com.ics342.weather

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.ics342.weather.databinding.ActivityMainBinding
import com.ics342.weather.domains.CurrentConditions
import com.ics342.weather.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.forecastButton.setOnClickListener {
            startActivity(Intent(this, ForecastActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.currentConditions.observe(this) { currentConditions ->
            bindData(currentConditions)
        }
        viewModel.loadData()
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
