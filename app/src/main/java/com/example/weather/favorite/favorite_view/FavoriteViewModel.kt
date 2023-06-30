package com.example.weather.favorite.favorite_view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.favorite.favorite_repo.FavoriteRepoInterface
import com.example.weather.search.search_repo.search_result_pojo.CityPojo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoriteViewModel(private var repo: FavoriteRepoInterface) : ViewModel() {
    private var _localCities = MutableStateFlow<List<CityPojo>>(listOf())
    val citiesList: StateFlow<List<CityPojo>>
        get() = _localCities

    fun getCities() {
        viewModelScope.launch {
            repo.getSavedCities().collect {
                _localCities.value = it
            }
        }
    }

    fun deleteCity(city: CityPojo) {
        viewModelScope.launch {
            repo.removeCity(city)
        }
    }
}