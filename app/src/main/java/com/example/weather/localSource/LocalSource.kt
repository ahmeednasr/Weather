package com.example.weather.localSource

import com.example.weather.map.repo.search_result_pojo.CityPojo

interface LocalSource {
    suspend fun getLocalCities(): List<CityPojo>
    suspend fun insertCity(city: CityPojo)
    suspend fun removeCity(city: CityPojo)
    fun getLocationType(): String
    fun getMapDetails(): Pair<Double, Double>
    fun getSpeedUnit(): String
    fun getTempUnit(): String
}