package com.example.weather.home.location_weather_view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LocationWeatherFactory(private val _repo: com.example.weather.data_source.location_weather_repo.LocationWeatherRepoInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LocationWeatherViewModel::class.java)) {
            LocationWeatherViewModel(_repo) as T
        } else {
            throw IllegalArgumentException("ViewModel Class not found")
        }

    }
}