package com.ics342.weather.fragments

import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.*
import com.ics342.weather.R
import com.ics342.weather.databinding.FragmentSearchBinding
import com.ics342.weather.domains.CurrentConditions
import com.ics342.weather.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var binding: FragmentSearchBinding
    @Inject lateinit var viewModel: SearchViewModel
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

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
        }
    }

    private fun navigateToCurrentConditions(currentConditions: CurrentConditions) {
        val zipCode = viewModel.getZipCode()
        val action = SearchFragmentDirections.actionSearchFragmentToCurrentConditionsFragment(
            zipCode,
            currentConditions
        )

        findNavController().navigate(action)
    }

    private fun getCurrentLocation() {
        // check if we have permission
        if (checkPermissions()) {
            // we should check location, but not yet
            // continue to get location
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location == null) {
                    Log.i("Location", "null location")
                } else {
                    Log.i("Location", location.toString())
                }
            }
        } else {
            // we do not have permission data, call to get permission
            requestPermission().launch(ACCESS_COARSE_LOCATION)
        }
    }

    private fun checkPermissions() = ActivityCompat.checkSelfPermission(requireContext(), ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    private fun requestPermission(): ActivityResultLauncher<String> {
        return registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("permission", "granted")
                getCurrentLocation()
            } else {
                Log.i("permission: ", "not granted")
            }
        }
    }
}
