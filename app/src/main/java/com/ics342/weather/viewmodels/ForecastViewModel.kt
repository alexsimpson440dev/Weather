package com.ics342.weather.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ics342.weather.interfaces.Api
import com.ics342.weather.domains.Forecast
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class ForecastViewModel @Inject constructor(private val service: Api): ViewModel() {

    private val _forecast = MutableLiveData<Forecast>()
    val forecast: LiveData<Forecast>
        get() = _forecast

    fun loadData(zipCode: String) = runBlocking {
        launch { _forecast.value = service.getForecast(zipCode) }
    }
}
