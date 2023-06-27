package com.example.weather.home.location_weather_repo.location_weather_pojo

data class Current(
    var clouds: Int,
    var dew_point: Double,
    var dt: Int,
    var feels_like: Double,
    var humidity: Int,
    var pressure: Int,
    var sunrise: Int,
    var sunset: Int,
    var temp: Double,
    var uvi: Double,
    var visibility: Int,
    var weather: List<Weather>,
    var wind_deg: Int,
    var wind_gust: Double,
    var wind_speed: Double
)