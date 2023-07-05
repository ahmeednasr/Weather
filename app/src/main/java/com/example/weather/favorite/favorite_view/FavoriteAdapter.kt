package com.example.weather.favorite.favorite_view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.databinding.FavCityItemBinding
import com.example.weather.map.repo.search_result_pojo.CityPojo

class FavoriteAdapter(private val listener: OnCityClickListener) :
    ListAdapter<CityPojo, FavoriteAdapter.ViewHolder>(FavoriteAdapter.CityDiffUtil()) {
    lateinit var binding: FavCityItemBinding

    class ViewHolder(var binding: FavCityItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = FavCityItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentCity: CityPojo = getItem(position)
        holder.binding.cityName.text = currentCity.name

        val description = "${currentCity.country},${currentCity.state?:""}"
        holder.binding.cityDiscription.text = description
        holder.binding.remove.setOnClickListener {
            listener.removeCity(currentCity)
        }
        holder.binding.favCard.setOnClickListener {
            listener.viewCity(currentCity)
        }
    }

    class CityDiffUtil : DiffUtil.ItemCallback<CityPojo>() {
        override fun areItemsTheSame(oldItem: CityPojo, newItem: CityPojo): Boolean {
            return oldItem.lat == newItem.lat && oldItem.lon == newItem.lon
        }

        override fun areContentsTheSame(oldItem: CityPojo, newItem: CityPojo): Boolean {
            return oldItem == newItem
        }

    }

}