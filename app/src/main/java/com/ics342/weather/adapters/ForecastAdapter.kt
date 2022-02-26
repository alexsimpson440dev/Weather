package com.ics342.weather.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ics342.weather.R
import com.ics342.weather.databinding.ForecastDataBinding
import com.ics342.weather.domains.DayForecast
import com.ics342.weather.utils.getDateTime
import java.time.format.DateTimeFormatter

class ForecastAdapter(
    private var forecastData: List<DayForecast>
) : RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    @SuppressLint("NewApi")
    class ViewHolder(private val binding: ForecastDataBinding) : RecyclerView.ViewHolder(binding.root) {
        private val dateFormat = DateTimeFormatter.ofPattern("MMM dd")
        private val timeFormat = DateTimeFormatter.ofPattern("h:mma")

        private val imageView: ImageView = binding.conditionIcon

        fun bind(data: DayForecast) {
            binding.date.text = dateFormat.format(data.dt.getDateTime())
            binding.currentTemp.text = binding.currentTemp.context.getString(R.string.temp, data.temp.day)
            binding.high.text = binding.high.context.getString(R.string.high_colon, data.temp.max)
            binding.low.text = binding.low.context.getString(R.string.low_colon, data.temp.min)
            binding.sunrise.text = binding.sunrise.context.getString(
                R.string.sunrise,
                timeFormat.format(data.sunrise.getDateTime()).lowercase()
            )
            binding.sunset.text = binding.sunset.context.getString(
                R.string.sunset,
                timeFormat.format(data.sunset.getDateTime()).lowercase()
            )

            val iconName = data.weather.firstOrNull()?.icon
            val iconUrl = "https://openweathermap.org/img/wn/$iconName@2x.png"
            Glide.with(this.itemView.context)
                .load(iconUrl)
                .into(this.imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ForecastDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(forecastData[position])
    }

    override fun getItemCount() = forecastData.size

    fun appendData(newForecastData: DayForecast) {
        this.forecastData = this.forecastData + newForecastData
        notifyItemInserted(this.forecastData.lastIndex)
    }
}
