package com.ics342.weather.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ics342.weather.R
import com.ics342.weather.adapters.ForecastAdapter
import com.ics342.weather.domains.DayForecast
import com.ics342.weather.domains.Forecast
import com.ics342.weather.viewmodels.ForecastViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ForecastFragment : Fragment(R.layout.fragment_forecast) {

    private val args: ForecastFragmentArgs by navArgs()
    private lateinit var recyclerView: RecyclerView
    @Inject lateinit var viewModel: ForecastViewModel
    private var adapterData = mutableListOf<DayForecast>()
    private lateinit var adapter: ForecastAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view)
        adapter = ForecastAdapter(adapterData)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)
    }

    override fun onResume() {
        super.onResume()
        viewModel.forecast.observe(this) { forecast ->
            bindData(forecast)
        }
        viewModel.loadData(args.zipCode)
    }

    private fun bindData(forecast: Forecast) {
        for (i in 0 until forecast.list.count()) {
            adapter.appendData(forecast.list[i])
        }
    }
}
