package com.example.weather.data_source.location_weather_repo.location_weather_remote

import com.example.weather.data_source.location_weather_repo.location_weather_pojo.LocationWeatherResponse

interface LocationWeatherRemoteSource {
    suspend fun getCurrentLocationResponse(
        latitude: Double,
        longitude: Double,
        language: String,
    ): com.example.weather.data_source.location_weather_repo.location_weather_pojo.LocationWeatherResponse
}