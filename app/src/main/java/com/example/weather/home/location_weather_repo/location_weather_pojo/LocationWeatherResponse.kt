package com.example.weather.home.location_weather_repo.location_weather_pojo

data class LocationWeatherResponse(
    var current: Current,
    var daily: List<Daily>,
    var hourly: List<Hourly>,
    var lat: Double,
    var lon: Double,
    var timezone: String,
    var timezone_offset: Int
)