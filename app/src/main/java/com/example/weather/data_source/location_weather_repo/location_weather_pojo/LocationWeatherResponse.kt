package com.example.weather.data_source.location_weather_repo.location_weather_pojo

data class LocationWeatherResponse(
    var current: com.example.weather.data_source.location_weather_repo.location_weather_pojo.Current,
    var daily: List<com.example.weather.data_source.location_weather_repo.location_weather_pojo.Daily>,
    var hourly: List<com.example.weather.data_source.location_weather_repo.location_weather_pojo.Hourly>,
    var lat: Double,
    var lon: Double,
    var timezone: String,
    var timezone_offset: Int
)