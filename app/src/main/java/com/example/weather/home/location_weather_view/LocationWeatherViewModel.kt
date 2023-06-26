package com.example.weather.home.location_weather_view

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.home.location_weather_repo.LocationWeatherApiState
import com.example.weather.home.location_weather_repo.LocationWeatherRepoInterface
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

// AllProductViewModel(private var repo: Repo) best for Testing ,loosely coupled

class LocationWeatherViewModel(private var repo: LocationWeatherRepoInterface) : ViewModel() {
    private var _postStateFlow =
        MutableStateFlow<LocationWeatherApiState>(LocationWeatherApiState.Loading)
    val responseFlow: StateFlow<LocationWeatherApiState> = _postStateFlow
    private val _postTimeStateFlow = MutableStateFlow(0L)
    val currentTimeStateFlow: StateFlow<Long> = _postTimeStateFlow
    init {
        Log.i("MYTAG", "init HomeViewModel")
        //getTime()
    }
     fun getTime(){
        viewModelScope.launch {
            while (true){
                _postTimeStateFlow.value=System.currentTimeMillis()
                delay(1000)
            }
        }
    }
    fun getCurrentLocationResponse(
        latitude: Double, longitude: Double, units: String, language: String
    ) {
        viewModelScope.launch {
            Log.i("MYTAG", "viewModelScope.launch in HomeViewModel")
            try {
                repo.getCurrentLocationResponse(latitude, longitude, units, language)
                    .collect { data ->
                        _postStateFlow.value = LocationWeatherApiState.Success(data)
                    }

            } catch (e: Exception) {
                _postStateFlow.value = LocationWeatherApiState.Failure(e)
            }
//            repo.getCurrentLocationResponse(latitude, longitude, units, language)
//                .catch { e ->
//                    _postStateFlow.value = LocationWeatherApiState.Failure(e)
//                }.collect { data ->
//                    _postStateFlow.value = LocationWeatherApiState.Success(data)
//                }
        }

    }

    override fun onCleared() {
        super.onCleared()
        Log.i("MYTAG", "on clear HomeViewModel")
    }
}