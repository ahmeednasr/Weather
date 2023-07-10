package com.example.weather.data_source.alert_repo.alert_remote

import com.example.weather.data_source.alert_repo.alert_pojo.AlertResponse
import com.example.weather.data_source.location_weather_repo.location_weather_remote.LocationWeatherApiClient
import com.example.weather.data_source.location_weather_repo.location_weather_remote.LocationWeatherApiService
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AlertApiClient : AlertRemoteSource {

    init {
        val gson = Gson()
        retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BASE_URL)
            .build()
            .create(AlertApiService::class.java)
    }

    companion object {
        private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
        private lateinit var retrofit: AlertApiService
        private var apiClient: AlertApiClient? = null
        fun getInstance(): AlertApiClient {
            return apiClient
                ?: synchronized(this) {
                    val tmp =
                        AlertApiClient()
                    apiClient = tmp
                    tmp
                }
        }
    }

    override suspend fun getAlarms(latitude: Double, longitude: Double): AlertResponse {

        return retrofit.getAlerts(latitude,longitude)
    }
}