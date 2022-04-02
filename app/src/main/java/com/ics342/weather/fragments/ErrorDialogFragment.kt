package com.ics342.weather.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.ics342.weather.R

class ErrorDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.location_error))
            .setPositiveButton(R.string.ok, null)
            .create()

    companion object {
        const val TAG = "ErrorDialogFragment"
    }
}
