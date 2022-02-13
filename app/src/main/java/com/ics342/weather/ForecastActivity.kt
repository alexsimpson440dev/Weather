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
        DayForecast(1644148800L, 1644148800L, 1645498800, ForecastTemp(69F, 62F, 85F)),
        DayForecast(1644235200L, 1644148800L, 1645498800, ForecastTemp(70F, 65F, 88F)),
        DayForecast(1644321600L, 1644148800L, 1645498800, ForecastTemp(71F, 70F, 90F)),
        DayForecast(1644408000L, 1644148800L, 1645498800, ForecastTemp(70F, 75F, 80F)),
        DayForecast(1644494400L, 1644148800L, 1645498800, ForecastTemp(85F, 80F, 86F)),
        DayForecast(1644580800L, 1644148800L, 1645498800, ForecastTemp(88F, 83F, 85F)),
        DayForecast(1644667200L, 1644148800L, 1645498800, ForecastTemp(66F, 66F, 75F)),
        DayForecast(1644753600L, 1644148800L, 1645498800, ForecastTemp(72F, 70F, 76F)),
        DayForecast(1644840000L, 1644148800L, 1645498800, ForecastTemp(75F, 65F, 75F)),
        DayForecast(1644926400L, 1644148800L, 1645498800, ForecastTemp(79F, 70F, 80F)),
        DayForecast(1645012800L, 1644148800L, 1645498800, ForecastTemp(68F, 65F, 70F)),
        DayForecast(1645099200L, 1644148800L, 1645498800, ForecastTemp(70F, 69F, 71F)),
        DayForecast(1645185600L, 1644148800L, 1645498800, ForecastTemp(69F, 55F, 69F)),
        DayForecast(1645272000L, 1644148800L, 1645498800, ForecastTemp(73F, 70F, 74F)),
        DayForecast(1645358400L, 1644148800L, 1645498800, ForecastTemp(79F, 74F, 80F)),
        DayForecast(1645444800L, 1644148800L, 1645498800, ForecastTemp(85F, 80F, 86F))
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.adapter = ForecastAdapter(adapterData, this)
    }
}
