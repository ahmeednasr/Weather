package com.example.weather.home.location_weather_repo.remote

import com.example.weather.companion.MyCompanion
import com.example.weather.home.location_weather_repo.location_weather_pojo.LocationWeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationWeatherApiService {
    @GET("onecall")
    suspend fun getCurrentLocationWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units")units:String,
        @Query("lang") language: String,
        @Query("exclude") exclude: String="minutely",
        @Query("appid") apiKey: String =MyCompanion.API_Key
    ): LocationWeatherResponse
}