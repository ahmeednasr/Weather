package com.example.weather.data.source

import com.example.weather.data_source.location_weather_repo.current_pojo.*
import com.example.weather.data_source.location_weather_repo.location_weather_pojo.*
import com.example.weather.data_source.location_weather_repo.location_weather_pojo.Weather

class FakeResponse {
    companion object {
        val lat = 37.7749
        val lon = -122.4194
        val timezone = "America/Los_Angeles"
        val timezone_offset = -25200

        val weatherList = listOf(Weather("clear", "Clear Sky", 2, ""))
        val feelsLike = FeelsLike(2.5, 0.0, 0.0, 0.0)
        val temp = Temp(2.5, 0.0, 0.0, 0.0, 0.0, 9.0)
        val main = Main(45.66, 7, 15, 99, 98, 9.8, 0.0, -0.1)
        val currentCity = CurrentPojo(
            "stations", Clouds(0), 200, Coord(lat, lon), 509889, 360, main, "Los_Angeles",
            Sys("USA", 8866697, 8444775),
            timezone_offset, 65,
            listOf(), Wind(6, 4.3, 7.8)
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

        val locationWeatherResponse = LocationWeatherResponse(
            currentCity.name,
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