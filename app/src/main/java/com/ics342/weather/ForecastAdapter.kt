package com.ics342.weather

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ics342.weather.domains.DayForecast
import com.ics342.weather.utils.getDateTime
import java.time.format.DateTimeFormatter

class ForecastAdapter(
    private val forecastData: List<DayForecast>,
    private val context: Context
) : RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    @SuppressLint("NewApi")
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val dateFormat = DateTimeFormatter.ofPattern("MMM dd")
        private val timeFormat = DateTimeFormatter.ofPattern("HH:mm")

        private val dateView: TextView = view.findViewById(R.id.date)
        private val currentTempView: TextView = view.findViewById(R.id.current_temp)
        private val highTempView: TextView = view.findViewById(R.id.high)
        private val lowTempView: TextView = view.findViewById(R.id.low)
        private val sunriseTimeView: TextView = view.findViewById(R.id.sunrise)
        private val sunsetTimeView: TextView = view.findViewById(R.id.sunset)


        fun bind(data: DayForecast) {
            dateView.text = dateFormat.format(data.date.getDateTime())
            currentTempView.text = currentTempView.context.getString(R.string.temp, data.temp.day)
            highTempView.text = highTempView.context.getString(R.string.high_colon, data.temp.max)
            lowTempView.text = lowTempView.context.getString(R.string.low_colon, data.temp.min)
            sunriseTimeView.text = sunriseTimeView.context.getString(R.string.sunrise, timeFormat.format(data.sunrise.getDateTime()))
            sunsetTimeView.text = sunsetTimeView.context.getString(R.string.sunset, timeFormat.format(data.sunset.getDateTime()))
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