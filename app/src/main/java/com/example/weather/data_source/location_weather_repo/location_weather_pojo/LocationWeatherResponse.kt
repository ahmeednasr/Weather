package com.example.weather.data_source.location_weather_repo.location_weather_pojo

import java.io.Serializable

data class LocationWeatherResponse(
    var name: String = "",
    var current: Current,
    var daily: List<Daily>,
    var hourly: List<Hourly>,
    var lat: Double,
    var lon: Double,
    var timezone: String,
    var timezone_offset: Int
) : Serializable