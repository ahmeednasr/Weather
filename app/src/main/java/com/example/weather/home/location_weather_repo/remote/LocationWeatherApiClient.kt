package com.example.weather.home.location_weather_repo.remote

import android.util.Log
import com.example.weather.home.location_weather_repo.location_weather_pojo.LocationWeatherResponse
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LocationWeatherApiClient : LocationWeatherRemoteSource {
    init {
        val gson: Gson = Gson()
        retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BASE_URL)
            .build()
            .create(LocationWeatherApiService::class.java)
        Log.i("MYTAG", "init in HomeApiClient")
    }

    companion object {
        private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
        private lateinit var retrofit: LocationWeatherApiService
        private var apiClient: LocationWeatherApiClient? = null
        fun getInstance(): LocationWeatherApiClient {
            return apiClient ?: synchronized(this) {
                Log.i("MYTAG", "getInstance() in HomeApiClient")
                val tmp = LocationWeatherApiClient()
                apiClient = tmp
                tmp
            }
        }
    }

    override suspend fun getCurrentLocationResponse(
        latitude: Double,
        longitude: Double,
        units: String,
        language: String,
    ): LocationWeatherResponse {
        val response=retrofit.getCurrentLocationWeather(latitude, longitude, units, language)
        Log.i("MYTAG", "getCurrentLocationResponse() in HomeApiClient$response")
        return response
    }
}