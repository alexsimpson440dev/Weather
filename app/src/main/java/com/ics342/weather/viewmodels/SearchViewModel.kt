package com.ics342.weather.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject
import kotlin.random.Random

class SearchViewModel @Inject constructor() : ViewModel() {

    private val zipCode: String? = null

    private val _enableButton = MutableLiveData(false)
    private val _showErrorDialog = MutableLiveData(false)

    val showErrorDialog: LiveData<Boolean>
        get() = _showErrorDialog

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

    fun submitButtonClicked() {
        Log.d(this::class.simpleName, zipCode ?: "No Zip yet!")
        _showErrorDialog.value = Random.nextBoolean()
    }
}