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
        DayForecast(1L, 2L, 3L, ForecastTemp(69F, 65F, 85F), 1F, 5),
        DayForecast(2L, 2L, 3L, ForecastTemp(70F, 65F, 88F), 1F, 5),
        DayForecast(3L, 2L, 3L, ForecastTemp(69F, 65F, 85F), 1F, 5),
        DayForecast(4L, 2L, 3L, ForecastTemp(70F, 65F, 88F), 1F, 5),
        DayForecast(5L, 2L, 3L, ForecastTemp(69F, 65F, 85F), 1F, 5),
        DayForecast(6L, 2L, 3L, ForecastTemp(70F, 65F, 88F), 1F, 5),
        DayForecast(7L, 2L, 3L, ForecastTemp(69F, 65F, 85F), 1F, 5),
        DayForecast(8L, 2L, 3L, ForecastTemp(70F, 65F, 88F), 1F, 5),
        DayForecast(9L, 2L, 3L, ForecastTemp(69F, 65F, 85F), 1F, 5),
        DayForecast(10L, 2L, 3L, ForecastTemp(70F, 65F, 88F), 1F, 5),
        DayForecast(11L, 2L, 3L, ForecastTemp(69F, 65F, 85F), 1F, 5),
        DayForecast(12L, 2L, 3L, ForecastTemp(70F, 65F, 88F), 1F, 5),
        DayForecast(13L, 2L, 3L, ForecastTemp(69F, 65F, 85F), 1F, 5),
        DayForecast(14L, 2L, 3L, ForecastTemp(70F, 65F, 88F), 1F, 5),
        DayForecast(15L, 2L, 3L, ForecastTemp(69F, 65F, 85F), 1F, 5),
        DayForecast(16L, 2L, 3L, ForecastTemp(70F, 65F, 88F), 1F, 5)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.adapter = ForecastAdapter(adapterData)
    }
}
