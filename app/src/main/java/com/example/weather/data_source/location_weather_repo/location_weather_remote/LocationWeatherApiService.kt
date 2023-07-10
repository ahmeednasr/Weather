package com.example.weather.data_source.location_weather_repo.location_weather_remote

import com.example.weather.data_source.location_weather_repo.location_weather_pojo.LocationWeatherResponse
import com.example.weather.system.companion.MyCompanion
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationWeatherApiService {
    @GET("onecall")
    suspend fun getCurrentLocationWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("lang") language: String,
        @Query("exclude") exclude: String="minutely",
        @Query("appid") apiKey: String = MyCompanion.API_Key
    ): LocationWeatherResponse
}