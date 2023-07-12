package com.example.weather.data.source

import com.example.weather.data_source.location_weather_repo.location_weather_pojo.*

class FakeResponse {
    companion object {
        val weatherList = listOf(Weather("clear", "Clear Sky", 2, ""))
        val feelsLike = FeelsLike(2.5, 0.0, 0.0, 0.0)
        val temp = Temp(2.5, 0.0, 0.0, 0.0, 0.0, 9.0)
        val hour = Hourly(
            123456,
            25.0,
            5058894,
            0.0,
            15,
            0.0,
            16,
            56.9,
            0.0,
            155,
            weatherList, 0,
            0.0,
            26.6
        )
        val day = Daily(
            123456,
            80.0,
            1000,
            feelsLike,
            10,
            0.0,
            0,
            0,
            0.0,
            0,
            0,
            0,
            temp,
            0.0,
            weatherList,
            0,
            0.0, .9
        )
        val current = Current(
            123456,
            25.0,
            509889,
            10.0,
            1000,
            80,
            0,
            0,
            56.7,
            0.0,
            666,
            weatherList,
            89,
            0.0,
            45.7
        )
        val daily = listOf(
            day, day, day, day, day, day, day, day, day,
        )
        val hourly = listOf(
            hour,
            hour,
            hour,
            hour,
            hour,
            hour,
            hour,
            hour,
            hour,
            hour,
            hour,
            hour,
            hour,
            hour,
            hour,
            hour,
            hour,
            hour,
            hour,
            hour,
            hour,
            hour,
            hour,
            hour,
            hour,
            hour,
            hour,
        )
        val lat = 37.7749
        val lon = -122.4194
        val timezone = "America/Los_Angeles"
        val timezone_offset = -25200

        val locationWeatherResponse = LocationWeatherResponse(
            current,
            daily,
            hourly,
            lat,
            lon,
            timezone,
            timezone_offset
        )
    }
}