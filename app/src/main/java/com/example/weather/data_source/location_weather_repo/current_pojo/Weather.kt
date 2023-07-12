package com.example.weather.data_source.location_weather_repo.current_pojo

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)