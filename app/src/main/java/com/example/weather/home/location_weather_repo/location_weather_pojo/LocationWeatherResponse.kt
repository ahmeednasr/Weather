package com.example.weather.home.location_weather_repo.location_weather_pojo

data class LocationWeatherResponse(
    val current: Current,
    var daily: List<Daily>,
    var hourly: List<Hourly>,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int
)