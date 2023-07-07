package com.example.weather.data_source.location_weather_repo

import com.example.weather.data_source.location_weather_repo.location_weather_pojo.LocationWeatherResponse
import kotlinx.coroutines.flow.Flow

interface LocationWeatherRepoInterface {
    suspend fun getCurrentLocationResponse(
        latitude: Double,
        longitude: Double,
        language: String,
    ): Flow<com.example.weather.data_source.location_weather_repo.location_weather_pojo.LocationWeatherResponse>
    fun getLocationType(): String
    fun getMapDetails(): Pair<Double, Double>
    fun getSpeedUnit(): String
    fun getTempUnit(): String
}