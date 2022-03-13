package com.ics342.weather.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ics342.weather.R
import com.ics342.weather.fragments.CurrentConditionsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentConditionsFragment = CurrentConditionsFragment()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, currentConditionsFragment)
            commit()
        }
    }
}
