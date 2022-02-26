package com.ics342.weather.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ics342.weather.interfaces.Api
import com.ics342.weather.domains.CurrentConditions
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class MainViewModel @Inject constructor(private val service: Api): ViewModel() {

    private val _currentConditions = MutableLiveData<CurrentConditions>()
    val currentConditions: LiveData<CurrentConditions>
        get() = _currentConditions

    fun loadData() = runBlocking {
        launch { _currentConditions.value = service.getCurrentConditions("55304") }
    }
}