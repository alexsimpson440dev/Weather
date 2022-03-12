package com.ics342.weather.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ics342.weather.adapters.ForecastAdapter
import com.ics342.weather.R
import com.ics342.weather.domains.DayForecast
import com.ics342.weather.domains.Forecast
import com.ics342.weather.viewmodels.ForecastViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ForecastActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    @Inject lateinit var viewModel: ForecastViewModel
    private var adapterData = mutableListOf<DayForecast>()
    private lateinit var adapter: ForecastAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        recyclerView = findViewById(R.id.recycler_view)
        adapter = ForecastAdapter(adapterData)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        viewModel.forecast.observe(this) { forecast ->
            bindData(forecast)
        }
        viewModel.loadData()
    }

    private fun bindData(forecast: Forecast) {
        for (i in 0 until forecast.list.count()) {
            adapter.appendData(forecast.list[i])
        }
    }
}
