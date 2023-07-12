package com.example.weather.views.location_weather_view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.data_source.ConnectionResult
import com.example.weather.data_source.location_weather_repo.LocationWeatherApiState
import com.example.weather.data_source.location_weather_repo.LocationWeatherRepoInterface
import com.example.weather.data_source.location_weather_repo.current_pojo.CurrentPojo
import com.example.weather.data_source.location_weather_repo.location_weather_pojo.LocationWeatherResponse
import com.example.weather.data_source.succeeded
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.stream.Stream

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

    private var _postCurrentCityResult =
        MutableStateFlow<ConnectionResult<CurrentPojo>>(ConnectionResult.Loading)
    val currentCityResult: StateFlow<ConnectionResult<CurrentPojo>>
        get() = _postCurrentCityResult

    fun getCurrentLocationResponse(
        latitude: Double, longitude: Double, language: String
    ) {
        _postStateFlow.value =
            LocationWeatherApiState.Loading
        viewModelScope.launch {
            getCurrentCity(latitude, longitude, language)
            try {
                repo.getCurrentLocationResponse(latitude, longitude, language)
                    .collect { data ->
                        currentCityResult.collect {
                            when (it) {
                                is ConnectionResult.Success -> {
                                    data.name = it.data.name
                                    _postStateFlow.value =
                                        LocationWeatherApiState.Success(
                                            data
                                        )
                                }
                                else -> {

                                }
                            }

                        }

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

    fun getCurrentCity(latitude: Double, longitude: Double, language: String) {
        viewModelScope.launch {
            try {
                val response = repo.getCurrentCity(latitude, longitude, language)
                Log.i("MYNAME", "responce in viewModel: ${response}")
                _postCurrentCityResult.value = response
                Log.i("MYNAME", "responce in post: ${_postCurrentCityResult.value}")
                Log.i("MYNAME", "responce in flow: ${currentCityResult.value}")
            } catch (e: Exception) {
                _postCurrentCityResult.value = ConnectionResult.Loading
            }
        }
    }

    fun getCurrentTime(timezoneOffset: Int) {
        _postTimeStateFlow.value = 0L
        val currentTimeMillis =
            System.currentTimeMillis()
        val offsetInMillis = timezoneOffset * 1000L
        var td =
            (currentTimeMillis + offsetInMillis) / 1000
        _postTimeStateFlow.value = td

    }


    fun getLocationType(): String = repo.getLocationType()
    fun setLocationType(key: String) = repo.setLocationType(key)
    fun getMapDetails(): Pair<Double, Double> = repo.getMapDetails()
    fun getSpeedUnit(): String = repo.getSpeedUnit()
    fun getTempUnit(): String = repo.getTempUnit()
    fun getCachedResponse(): LocationWeatherResponse? = repo.getCachedResponse()
    fun cacheResponse(response: LocationWeatherResponse) = repo.cacheResponse(response)

    override fun onCleared() {
        super.onCleared()
    }
}