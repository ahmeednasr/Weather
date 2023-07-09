package com.example.weather.home.location_weather_view_model

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.data_source.location_weather_repo.LocationWeatherApiState
import com.example.weather.data_source.location_weather_repo.LocationWeatherRepoInterface
import com.example.weather.data_source.location_weather_repo.location_weather_pojo.LocationWeatherResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.util.*

// AllProductViewModel(private var repo: Repo) best for Testing ,loosely coupled

class LocationWeatherViewModel(private var repo: LocationWeatherRepoInterface) :
    ViewModel() {
    private var _postStateFlow =
        MutableStateFlow<LocationWeatherApiState>(
            LocationWeatherApiState.Loading
        )
    val responseFlow: StateFlow<LocationWeatherApiState>
        get() = _postStateFlow

    private var _postTimeStateFlow = MutableStateFlow(0L)
    val currentTimeStateFlow: StateFlow<Long>
        get() = _postTimeStateFlow

    init {
    }

    fun getTime() {
        viewModelScope.launch {
            while (true) {
                //  _postTimeStateFlow.value = System.currentTimeMillis()
                delay(1000)
            }
        }
    }

    fun getCurrentTime(timezoneOffset: Int) {

        viewModelScope.launch {
            while (true) {
                val currentTimeMillis =
                    System.currentTimeMillis()
                val offsetInMillis = timezoneOffset * 1000L
                var td =
                    (currentTimeMillis + offsetInMillis) / 1000
                _postTimeStateFlow.value = td
                delay(1000)
            }
        }

    }


    fun getCurrentLocationResponse(
        latitude: Double, longitude: Double, language: String
    ) {
        _postStateFlow.value =
            LocationWeatherApiState.Loading
        viewModelScope.launch {
            try {
                repo.getCurrentLocationResponse(latitude, longitude, language)
                    .collect { data ->
                        _postStateFlow.value =
                            LocationWeatherApiState.Success(
                                data
                            )
                    }

            } catch (e: Exception) {
                Log.d("errMy", e.toString())
                _postStateFlow.value =
                    LocationWeatherApiState.Failure(
                        e
                    )
            }

        }
    }

    fun getLocationType(): String = repo.getLocationType()
    fun getMapDetails(): Pair<Double, Double> = repo.getMapDetails()
    fun getSpeedUnit(): String = repo.getSpeedUnit()
    fun getTempUnit(): String = repo.getTempUnit()
    fun getCachedResponse(): LocationWeatherResponse = repo.getCachedResponse()
    fun cacheResponse(response: LocationWeatherResponse) = repo.cacheResponse(response)

    override fun onCleared() {
        super.onCleared()
    }
}