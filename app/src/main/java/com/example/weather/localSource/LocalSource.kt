package com.example.weather.localSource

import com.example.weather.search.search_repo.search_result_pojo.CityPojo
import kotlinx.coroutines.flow.Flow

interface LocalSource {
    fun getLocalCities(): Flow<List<CityPojo>>
    suspend fun insertCity(city:CityPojo)
    suspend fun removeCity(city:CityPojo)
}