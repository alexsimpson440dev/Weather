package com.ics342.weather

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ics342.weather.domains.DayForecast
import com.ics342.weather.domains.ForecastTemp

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var forecastButton: Button
    private val adapterData = listOf(
        DayForecast(1L, 2L, 3L, ForecastTemp(1F, 0F, 3F), 1F, 5)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler_view)
//        forecastButton.setOnContextClickListener {
//            startActivity(Intent(this, ForecastActivity::class.java))
//        }
        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.adapter = MyAdapter(adapterData)
    }
}
