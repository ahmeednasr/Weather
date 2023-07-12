package com.example.weather.data_source.localSource

import com.example.weather.data_source.location_weather_repo.location_weather_pojo.LocationWeatherResponse
import com.example.weather.data_source.search_repo.search_result_pojo.CityPojo

interface LocalSource {
    suspend fun getLocalCities(): List<CityPojo>
    suspend fun insertCity(city: CityPojo)
    suspend fun removeCity(city: CityPojo)
    fun getLocationType(): String
    fun setLocationType(type: String)
    fun getMapDetails(): Pair<Double, Double>
    fun setMapDetails(lat: Double, long: Double)
    fun getSpeedUnit(): String
    fun getTempUnit(): String
    fun cacheResponse(response: LocationWeatherResponse)
    fun getCachedResponse(): LocationWeatherResponse

}