package com.example.weather.home.location_weather_repo.remote

import com.example.weather.home.location_weather_repo.location_weather_pojo.LocationWeatherResponse

interface LocationWeatherRemoteSource {
    suspend fun getCurrentLocationResponse(
        latitude: Double,
        longitude: Double,
        language: String,
    ): LocationWeatherResponse
}