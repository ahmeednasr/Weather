package com.example.weather.data_source.location_weather_repo

import com.example.weather.data_source.location_weather_repo.location_weather_pojo.LocationWeatherResponse

sealed class LocationWeatherApiState {
    class Success(val data: com.example.weather.data_source.location_weather_repo.location_weather_pojo.LocationWeatherResponse) : com.example.weather.data_source.location_weather_repo.LocationWeatherApiState()
    class Failure(val msg: Throwable) : com.example.weather.data_source.location_weather_repo.LocationWeatherApiState()
    object Loading : com.example.weather.data_source.location_weather_repo.LocationWeatherApiState()
}