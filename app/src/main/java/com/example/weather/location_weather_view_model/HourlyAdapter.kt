package com.example.weather.home.location_weather_view_model

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.weather.R
import com.example.weather.system.companion.MyCompanion
import com.example.weather.databinding.HourlyItemBinding
import java.text.SimpleDateFormat
import java.util.*

class HourlyAdapter(val context: Context, var tempUnit: String) :
    ListAdapter<com.example.weather.data_source.location_weather_repo.location_weather_pojo.Hourly, HourlyAdapter.ViewHolder>(HourlyDiffUtil()) {
    lateinit var binding: HourlyItemBinding

    class ViewHolder(var binding: HourlyItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = HourlyItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var currentHourly: com.example.weather.data_source.location_weather_repo.location_weather_pojo.Hourly = getItem(position)
        var icon = currentHourly.weather[0].icon

        holder.binding.hourlyTemp.text = MyCompanion.getTemp(tempUnit, currentHourly.temp)
        binding.hourlyTempUnit.text = when (tempUnit) {
            MyCompanion.C -> context.getString(R.string.tempunit_celsius)
            MyCompanion.F -> context.getString(R.string.tempunit_fahrenheit)
            MyCompanion.K -> context.getString(R.string.tempunit_kelvin)
            else -> null // Handle any other cases if necessary
        }
        holder.binding.hourlyDescription.text = currentHourly.weather[0].description
        Glide.with(context)
            .load(MyCompanion.getIconLink(icon))
            .apply(RequestOptions().override(50, 50))
            .error(R.drawable.ic_launcher_foreground)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.icon)
        val date = Date(currentHourly.dt * 1000L) // convert to milliseconds
        val outputFormat = SimpleDateFormat("h a", Locale.getDefault())
        val time = outputFormat.format(date)
        holder.binding.hour.text = time
    }

    class HourlyDiffUtil : DiffUtil.ItemCallback<com.example.weather.data_source.location_weather_repo.location_weather_pojo.Hourly>() {
        override fun areItemsTheSame(oldItem: com.example.weather.data_source.location_weather_repo.location_weather_pojo.Hourly, newItem: com.example.weather.data_source.location_weather_repo.location_weather_pojo.Hourly): Boolean {
            return oldItem.dt == newItem.dt
        }

        override fun areContentsTheSame(oldItem: com.example.weather.data_source.location_weather_repo.location_weather_pojo.Hourly, newItem: com.example.weather.data_source.location_weather_repo.location_weather_pojo.Hourly): Boolean {
            return oldItem == newItem
        }
    }
}