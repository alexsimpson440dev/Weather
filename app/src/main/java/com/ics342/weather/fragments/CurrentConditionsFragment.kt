package com.ics342.weather.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.ics342.weather.R
import com.ics342.weather.databinding.FragmentCurrentConditionsBinding
import com.ics342.weather.domains.CurrentConditions
import com.ics342.weather.viewmodels.CurrentConditionsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CurrentConditionsFragment : Fragment(R.layout.fragment_current_conditions) {

    private val args: CurrentConditionsFragmentArgs by navArgs()
    private lateinit var binding: FragmentCurrentConditionsBinding
    @Inject lateinit var viewModel: CurrentConditionsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCurrentConditionsBinding.bind(view)

        binding.forecastButton.setOnClickListener {
            navigateToForecast()
        }
    }

    override fun onResume() {
        super.onResume()
        bindData(args.currentConditions)
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

    private fun navigateToForecast() {
        val action = CurrentConditionsFragmentDirections.actionCurrentConditionsFragmentToForecastFragment(args.zipCode)

        findNavController().navigate(action)
    }
}
