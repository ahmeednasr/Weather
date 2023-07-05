package com.example.weather.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weather.map.repo.RepoInterface

class MapViewModelFactory(private val _repo: RepoInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            MapViewModel(_repo) as T
        } else {
            throw  IllegalArgumentException("ViewModel Class not found")
        }
    }
}