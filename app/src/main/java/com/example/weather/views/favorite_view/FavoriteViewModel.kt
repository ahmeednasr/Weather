package com.example.weather.views.favorite_view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.data_source.favorite_repo.FavoriteRepoInterface
import com.example.weather.data_source.search_repo.search_result_pojo.CityPojo
import kotlinx.coroutines.launch

class FavoriteViewModel(private var repo: FavoriteRepoInterface) : ViewModel() {

    private var _liveData = MutableLiveData<List<CityPojo>>()
    val citiesList: LiveData<List<CityPojo>>
        get() = _liveData

    init {
        //getCities()
    }

    fun getCities() {
        viewModelScope.launch {
            _liveData.postValue(repo.getSavedCities())
        }
    }

    fun deleteCity(city: CityPojo) {
        viewModelScope.launch {
            repo.removeCity(city)
            _liveData.postValue(repo.getSavedCities())
        }

    }

    fun insertCity(city: CityPojo) {
        viewModelScope.launch {
            repo.insertCity(city)
            _liveData.postValue(repo.getSavedCities())
        }
    }
}