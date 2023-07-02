package com.example.weather.home.location_weather_view

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
import com.example.weather.companion.MyCompanion
import com.example.weather.databinding.DailyItemBinding
import com.example.weather.home.location_weather_repo.location_weather_pojo.Daily
import com.example.weather.home.location_weather_repo.location_weather_pojo.Hourly
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class DailyAdapter(val context: Context, var tempUnit: String) :
    ListAdapter<Daily, DailyAdapter.ViewHolder>(DailyAdapter.DailyDiffUtil()) {
    lateinit var binding: DailyItemBinding

    class ViewHolder(var binding: DailyItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DailyItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var currentDay: Daily = getItem(position)
        var icon = currentDay.weather[0].icon
        val max = MyCompanion.getTemp(tempUnit, currentDay.temp.max)
        val min = MyCompanion.getTemp(tempUnit, currentDay.temp.min)
        binding.dayTempUnit.text = when (tempUnit) {
            MyCompanion.C -> context.getString(R.string.tempunit_celsius)
            MyCompanion.F -> context.getString(R.string.tempunit_fahrenheit)
            MyCompanion.K -> context.getString(R.string.tempunit_kelvin)
            else -> null // Handle any other cases if necessary
        }

        val date = Date(currentDay.dt * 1000L)
        val calendar = Calendar.getInstance()
        val isCurrentDay =
            calendar.get(Calendar.DAY_OF_YEAR) == (calendar.timeInMillis / 86400000).toInt()
        val outputFormat = SimpleDateFormat("EEE", Locale.getDefault())
        val time = outputFormat.format(date)
        holder.binding.day.text = time
//        when (position) {
//            0 -> {
//        holder.binding.day.text = context.getString(R.string.Tomorrow)
//            }
//            else -> {
//                holder.binding.day.text = time
//            }
//        }
        holder.binding.dayTemp.text = "$max/$min"
        holder.binding.dayDescription.text = currentDay.weather[0].description
        Glide.with(context)
            .load(MyCompanion.getIconLink(icon))
            .apply(RequestOptions().override(50, 50))
            .error(R.drawable.ic_launcher_foreground)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.icon)
    }

    class DailyDiffUtil : DiffUtil.ItemCallback<Daily>() {
        override fun areItemsTheSame(oldItem: Daily, newItem: Daily): Boolean {
            return oldItem.dt == newItem.dt
        }

        override fun areContentsTheSame(oldItem: Daily, newItem: Daily): Boolean {
            return oldItem == newItem
        }
    }
}