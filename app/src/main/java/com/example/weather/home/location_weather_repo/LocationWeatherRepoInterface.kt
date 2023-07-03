package com.example.weather.home.location_weather_repo

import com.example.weather.home.location_weather_repo.location_weather_pojo.LocationWeatherResponse
import kotlinx.coroutines.flow.Flow

interface LocationWeatherRepoInterface {
    suspend fun getCurrentLocationResponse(
        latitude: Double,
        longitude: Double,
        language: String,
    ): Flow<LocationWeatherResponse>
    fun getLocationType(): String
    fun getMapDetails(): Pair<Double, Double>
    fun getSpeedUnit(): String
    fun getTempUnit(): String
}