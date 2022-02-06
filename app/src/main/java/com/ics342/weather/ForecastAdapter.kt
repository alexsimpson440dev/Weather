package com.ics342.weather

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ics342.weather.domains.DayForecast
import com.ics342.weather.utils.getDateTime
import java.time.format.DateTimeFormatter

class ForecastAdapter(private val forecastData: List<DayForecast>) : RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    @SuppressLint("NewApi")
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val dateFormat = DateTimeFormatter.ofPattern("MMM dd")
        private val timeFormat = DateTimeFormatter.ofPattern("HH:mm")

        private val dateView: TextView = view.findViewById(R.id.date)
        private val currentTemp: TextView = view.findViewById(R.id.current_temp)

        fun bind(data: DayForecast) {
            dateView.text = dateFormat.format(data.date.getDateTime())
            currentTemp.text = data.temp.day.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context).inflate(R.layout.forecast_data, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(forecastData[position])
    }

    override fun getItemCount() = forecastData.size
}