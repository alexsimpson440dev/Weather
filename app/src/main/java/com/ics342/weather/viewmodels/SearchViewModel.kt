package com.ics342.weather.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {

    private val _enableButton = MutableLiveData(false)
    private val zipCode: String? = null

    val enableButton: LiveData<Boolean>
        get() = _enableButton

    fun updateZipCode(zipCode: String) {
        if (zipCode != this.zipCode) {
            _enableButton.value = isValidZipCode(zipCode)
        }
    }

    private fun isValidZipCode(zipCode: String): Boolean {
        return zipCode.length == 5 && zipCode.all { it.isDigit() }
    }
}