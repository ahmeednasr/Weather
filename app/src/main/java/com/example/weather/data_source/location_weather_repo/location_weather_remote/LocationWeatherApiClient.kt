package com.example.weather.data_source.location_weather_repo.location_weather_remote

import android.util.Log
import com.example.weather.data_source.location_weather_repo.location_weather_pojo.LocationWeatherResponse
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LocationWeatherApiClient :
    com.example.weather.data_source.location_weather_repo.location_weather_remote.LocationWeatherRemoteSource {
    init {
        val gson: Gson = Gson()
        com.example.weather.data_source.location_weather_repo.location_weather_remote.LocationWeatherApiClient.Companion.retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(com.example.weather.data_source.location_weather_repo.location_weather_remote.LocationWeatherApiClient.Companion.BASE_URL)
            .build()
            .create(com.example.weather.data_source.location_weather_repo.location_weather_remote.LocationWeatherApiService::class.java)
    }

    companion object {
        private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
        private lateinit var retrofit: com.example.weather.data_source.location_weather_repo.location_weather_remote.LocationWeatherApiService
        private var apiClient: com.example.weather.data_source.location_weather_repo.location_weather_remote.LocationWeatherApiClient? = null
        fun getInstance(): com.example.weather.data_source.location_weather_repo.location_weather_remote.LocationWeatherApiClient {
            return com.example.weather.data_source.location_weather_repo.location_weather_remote.LocationWeatherApiClient.Companion.apiClient
                ?: synchronized(this) {
                val tmp =
                    com.example.weather.data_source.location_weather_repo.location_weather_remote.LocationWeatherApiClient()
                com.example.weather.data_source.location_weather_repo.location_weather_remote.LocationWeatherApiClient.Companion.apiClient = tmp
                tmp
            }
        }
    }

    override suspend fun getCurrentLocationResponse(
        latitude: Double,
        longitude: Double,
        language: String,
    ): com.example.weather.data_source.location_weather_repo.location_weather_pojo.LocationWeatherResponse {
        val response=
            com.example.weather.data_source.location_weather_repo.location_weather_remote.LocationWeatherApiClient.Companion.retrofit.getCurrentLocationWeather(latitude, longitude, language)
        Log.d("errMy",response.toString())
        return response
    }
}