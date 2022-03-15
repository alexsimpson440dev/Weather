package com.ics342.weather.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ics342.weather.R
import com.ics342.weather.databinding.FragmentSearchBinding
import com.ics342.weather.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var binding: FragmentSearchBinding
    @Inject lateinit var viewModel: SearchViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)

        viewModel.enableButton.observe(this) { enable ->
            binding.submitButton.isEnabled = enable
        }

        viewModel.showErrorDialog.observe(this) { showError ->
            if (showError) {
                ErrorDialogFragment().show(childFragmentManager, ErrorDialogFragment.TAG)
            }
        }

        binding.zipCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                p0?.toString()?.let { viewModel.updateZipCode(it) }
            }

            override fun afterTextChanged(p0: Editable?) {
                5
            }
        })

        binding.submitButton.setOnClickListener {
            viewModel.submitButtonClicked()
            findNavController().navigate(R.id.action_searchFragment_to_currentConditionsFragment)
        }
    }
}
