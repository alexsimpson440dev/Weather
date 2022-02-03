package com.ics342.weather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ics342.weather.domains.DayForecast
import com.ics342.weather.domains.ForecastTemp

class ForecastActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    private val adapterData = listOf(
        DayForecast(1L, 2L, 3L, ForecastTemp(1F, 0F, 3F), 1F, 5)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.adapter = ForecastAdapter(adapterData)
    }
}
