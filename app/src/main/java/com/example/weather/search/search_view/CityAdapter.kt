package com.example.weather.search.search_view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.databinding.SearchCityItemBinding
import com.example.weather.search.SaveCityListener
import com.example.weather.search.search_repo.search_result_pojo.CityPojo

class CityAdapter(val listener: SaveCityListener) :
    ListAdapter<CityPojo, CityAdapter.ViewHolder>(CityAdapter.CityDiffUtil()) {
    private lateinit var binding: SearchCityItemBinding

    class ViewHolder(var binding: SearchCityItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = SearchCityItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var currentCity: CityPojo = getItem(position)
        binding.cityName.text = currentCity.name
        binding.cityDiscription.text = "${currentCity.country},${currentCity.state}"
        binding.addFav.setOnClickListener {
            listener.onCityClick(currentCity)
        }
    }

    class CityDiffUtil : DiffUtil.ItemCallback<CityPojo>() {
        override fun areItemsTheSame(oldItem: CityPojo, newItem: CityPojo): Boolean {
            return oldItem.name == newItem.name && oldItem.country == newItem.country && oldItem.state == newItem.state
        }

        override fun areContentsTheSame(oldItem: CityPojo, newItem: CityPojo): Boolean {
            return oldItem == newItem
        }

    }


}