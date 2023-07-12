package com.example.weather.views.alerts_view

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.data_source.alert_repo.alert_pojo.SavedAlert
import com.example.weather.databinding.AlertItemBinding
import java.text.SimpleDateFormat
import java.util.*

class AlertAdapter(private val onRemove: OnAlertRemoveListener) :
    RecyclerView.Adapter<AlertAdapter.ViewHolder>() {
    var data = listOf<SavedAlert>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(val binding: AlertItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AlertItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentAlert: SavedAlert = data[position]
        holder.binding.cityName.text = currentAlert.cityName
        Log.i("time", "${currentAlert.startTime}")
        val date = Date(currentAlert.startTime * 1000L) // convert to milliseconds
        val dateFormat = SimpleDateFormat("h:mm a dd, MMM", Locale.getDefault())
        val time = dateFormat.format(date)
        holder.binding.startTime.text = time
        holder.binding.remove.setOnClickListener {
            onRemove.onRemove(currentAlert)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}