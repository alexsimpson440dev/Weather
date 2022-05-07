package com.ics342.weather.fragments

import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.*
import com.ics342.weather.R
import com.ics342.weather.databinding.FragmentSearchBinding
import com.ics342.weather.domains.CurrentConditions
import com.ics342.weather.services.WeatherService
import com.ics342.weather.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var binding: FragmentSearchBinding

    @Inject
    lateinit var viewModel: SearchViewModel
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (viewModel.enableNotifications.value == true) {
                    viewModel.setLocation(locationResult.lastLocation)
                    viewModel.locationDataObtained()
                    handleNotificationButton()
                }
            }
        }
        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        locationRequest.interval = 1800000 // this should be 30 minutes

        setNotificationButtonText()

        viewModel.enableButton.observe(viewLifecycleOwner) { enable ->
            binding.submitButton.isEnabled = enable
        }

        viewModel.showErrorDialog.observe(viewLifecycleOwner) { showError ->
            if (showError) {
                ErrorDialogFragment().show(childFragmentManager, ErrorDialogFragment.TAG)
            }
        }

        binding.zipCode.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                p0?.toString()?.let { viewModel.updateZipCode(it) }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.submitButton.setOnClickListener {
            viewModel.submitButtonClicked()
            viewModel.currentConditions.observe(viewLifecycleOwner) { currentConditions ->
                navigateToCurrentConditions(currentConditions)
            }
        }

        binding.getLocationButton.setOnClickListener {
            getCurrentLocation()

            viewModel.currentConditions.observe(viewLifecycleOwner) { currentConditions ->
                navigateToCurrentConditions(currentConditions)
            }
        }

        binding.notificationButton.setOnClickListener {
            viewModel.notificationsButtonClicked()
            handleNotificationButton()
        }
    }

    private fun navigateToCurrentConditions(currentConditions: CurrentConditions) {
        val zipCode = viewModel.getZipCode()
        val (latitude, longitude) = viewModel.getLocation()
        val action = SearchFragmentDirections.actionSearchFragmentToCurrentConditionsFragment(
            zipCode,
            latitude,
            longitude,
            currentConditions
        )

        findNavController().navigate(action)
    }

    private fun getCurrentLocation() {
        if (checkPermissions()) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermission()
                return
            }
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location == null) {
                    updateLocation()
                } else {
                    onLocationObtained(location)
                }
            }
        } else {
            requestPermission()
        }
    }

    private fun updateLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
        getCurrentLocation()
    }

    private fun checkPermissions() = ActivityCompat.checkSelfPermission(
        requireContext(),
        ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    private fun requestPermission() {
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.request_location_permission))
            .setNeutralButton(getString(R.string.ok)) { _, _ ->
                requestPermissions(
                    arrayOf(ACCESS_COARSE_LOCATION),
                    COARSE_LOCATION_ACCESS_CODE
                )
            }
            .create()
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (viewModel.enableNotifications.value == true) {
            handleNotificationButton()
            return
        }

        if (requestCode == COARSE_LOCATION_ACCESS_CODE) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun onLocationObtained(location: Location) {
        viewModel.setLocation(location)
        viewModel.locationDataObtained()
    }

    private fun handleNotificationButton() {
        if (viewModel.enableNotifications.value == true) {
            getCurrentLocation()
            setNotificationButtonText()
            if (checkPermissions()) {
                viewModel.currentConditions.observe(viewLifecycleOwner) { currentConditions ->
                    Intent(requireContext(), WeatherService::class.java).also { intent ->
                        intent.putExtra(
                            "temperature",
                            currentConditions.main.temp.roundToInt().toString()
                        )
                        intent.putExtra("location", currentConditions.name)
                        requireActivity().startService(intent)
                    }
                }
            } else {
                requestPermission()
            }
        } else {
            binding.notificationButton.text = getString(R.string.turn_on_notifications)
            Intent(requireContext(), WeatherService::class.java).also { intent ->
                requireActivity().stopService(intent)
            }
        }
    }

    private fun setNotificationButtonText() {
        val notificationStringId: Int = if (viewModel.enableNotifications.value == true) {
            R.string.turn_off_notifications
        } else {
            R.string.turn_on_notifications
        }

        binding.notificationButton.text = getString(notificationStringId)
    }

    companion object {
        private const val COARSE_LOCATION_ACCESS_CODE = 100
    }
}
