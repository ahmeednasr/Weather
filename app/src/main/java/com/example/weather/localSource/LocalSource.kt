package com.example.weather.localSource

import androidx.lifecycle.LiveData
import com.example.weather.search.search_repo.search_result_pojo.CityPojo
import kotlinx.coroutines.flow.Flow

interface LocalSource {
    suspend fun getLocalCities(): List<CityPojo>
    suspend fun insertCity(city: CityPojo)
    suspend fun removeCity(city: CityPojo)
    fun getLocationType(): String
    fun getMapDetails(): Pair<Double, Double>
    fun getSpeedUnit(): String
    fun getTempUnit(): String
}