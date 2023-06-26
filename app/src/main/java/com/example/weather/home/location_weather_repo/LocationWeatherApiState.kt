package com.example.weather.home.location_weather_repo

import com.example.weather.home.location_weather_repo.location_weather_pojo.LocationWeatherResponse

sealed class LocationWeatherApiState {
    class Success(val data: LocationWeatherResponse) : LocationWeatherApiState()
    class Failure(val msg: Throwable) : LocationWeatherApiState()
    object Loading : LocationWeatherApiState()
}