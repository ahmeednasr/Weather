package com.example.weather.data.source

import com.example.weather.data_source.location_weather_repo.current_pojo.CurrentPojo
import com.example.weather.data_source.location_weather_repo.location_weather_pojo.Current
import com.example.weather.data_source.location_weather_repo.location_weather_pojo.LocationWeatherResponse
import com.example.weather.data_source.location_weather_repo.location_weather_remote.LocationWeatherRemoteSource

class FakeRemoteDataSource(var response: LocationWeatherResponse, var currentCity: CurrentPojo) :
    LocationWeatherRemoteSource {
    override suspend fun getCurrentLocationResponse(
        latitude: Double,
        longitude: Double,
        language: String
    ): LocationWeatherResponse {
        return response
    }

    override suspend fun getCurrentCity(
        latitude: Double,
        longitude: Double,
        language: String
    ): CurrentPojo {
        return currentCity
    }
}