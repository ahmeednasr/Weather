package com.example.weather.data_source.location_weather_repo

import com.example.weather.data_source.ConnectionResult
import com.example.weather.data_source.location_weather_repo.current_pojo.CurrentPojo
import com.example.weather.data_source.location_weather_repo.location_weather_pojo.LocationWeatherResponse
import kotlinx.coroutines.flow.Flow

interface LocationWeatherRepoInterface {
    suspend fun getCurrentLocationResponse(
        latitude: Double,
        longitude: Double,
        language: String,
    ): Flow<LocationWeatherResponse>

    suspend fun getCurrentCity(
        latitude: Double,
        longitude: Double,
        language: String,
    ): ConnectionResult<CurrentPojo>
    fun setLocationType(key:String)
    fun getLocationType(): String
    fun getMapDetails(): Pair<Double, Double>
    fun getSpeedUnit(): String
    fun getTempUnit(): String
    fun cacheResponse(response: LocationWeatherResponse)
    fun getCachedResponse(): LocationWeatherResponse?
}