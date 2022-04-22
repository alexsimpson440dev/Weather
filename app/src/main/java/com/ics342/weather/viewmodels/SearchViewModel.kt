package com.ics342.weather.viewmodels

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ics342.weather.domains.CurrentConditions
import com.ics342.weather.interfaces.Api
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val service: Api) : ViewModel() {

    private var zipCode: String = ""
    private var latitude: String = ""
    private var longitude: String = ""

    private val _enableButton = MutableLiveData(false)
    private val _showErrorDialog = MutableLiveData(false)
    private var _currentConditions = MutableLiveData<CurrentConditions>()

    val showErrorDialog: LiveData<Boolean>
        get() = _showErrorDialog

    val enableButton: LiveData<Boolean>
        get() = _enableButton

    val currentConditions: LiveData<CurrentConditions>
        get() = _currentConditions

    fun getZipCode() = zipCode

    fun getLocation(): Pair<String, String> = Pair(latitude, longitude)

    fun updateZipCode(zipCode: String) {
        if (zipCode != this.zipCode) {
            _enableButton.value = isValidZipCode(zipCode)
            this.zipCode = zipCode
        }
    }

    fun setLocation(location: Location) {
        latitude = location.latitude.toString()
        longitude = location.longitude.toString()
    }

    private fun isValidZipCode(zipCode: String): Boolean {
        return zipCode.length == 5 && zipCode.all { it.isDigit() }
    }

    fun submitButtonClicked() = runBlocking {
        _showErrorDialog.value = false
        _currentConditions = MutableLiveData<CurrentConditions>()

        if (!isValidZipCode(zipCode)) {
            _showErrorDialog.value = true
            return@runBlocking
        }

        launch {
            try {
                _currentConditions.value = service.getCurrentConditions(zip = zipCode)
            } catch (ex: Exception) {
                _showErrorDialog.value = true
            }
        }
    }

    fun locationDataObtained() = runBlocking {
        launch {
            try {
                _currentConditions.value = service.getCurrentConditions(lat = latitude, lon = longitude)
            } catch (ex: Exception) {
                _showErrorDialog.value = true
            }
        }
    }
}
