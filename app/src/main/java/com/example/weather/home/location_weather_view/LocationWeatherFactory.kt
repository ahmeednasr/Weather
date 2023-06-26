package com.example.weather.home.location_weather_view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weather.home.location_weather_repo.LocationWeatherRepoInterface

class LocationWeatherFactory(private val _repo: LocationWeatherRepoInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LocationWeatherViewModel::class.java)) {
            LocationWeatherViewModel(_repo) as T
        } else {
            throw IllegalArgumentException("ViewModel Class not found")
        }

    }
}