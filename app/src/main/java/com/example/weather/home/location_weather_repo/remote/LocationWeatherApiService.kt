package com.example.weather.home.location_weather_repo.remote

import com.example.weather.home.location_weather_repo.location_weather_pojo.LocationWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationWeatherApiService {
    //weather?lat=30.03&lon=31.23&units=metric&lang=ar&appid=a117466978c46167246dc8d6a700cce6#
    @GET("onecall")
    suspend fun getCurrentLocationWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units")units:String,
        @Query("lang") language: String,
        @Query("exclude") exclude: String="minutely",
        @Query("appid") apiKey: String ="a117466978c46167246dc8d6a700cce6"
    ): LocationWeatherResponse
}