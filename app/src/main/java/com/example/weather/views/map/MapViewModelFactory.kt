package com.example.weather.views.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weather.data_source.search_repo.SearchRepoInterface

class MapViewModelFactory(private val _repo: SearchRepoInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            MapViewModel(_repo) as T
        } else {
            throw  IllegalArgumentException("ViewModel Class not found")
        }
    }
}