package com.ics342.weather

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    lateinit var forecastButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        forecastButton = findViewById(R.id.forecastButton)
        forecastButton.setOnClickListener {
            startActivity(Intent(this, ForecastActivity::class.java))
        }
    }
}
