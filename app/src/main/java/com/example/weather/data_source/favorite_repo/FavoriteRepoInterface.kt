package com.example.weather.data_source.favorite_repo

import com.example.weather.data_source.search_repo.search_result_pojo.CityPojo

interface FavoriteRepoInterface {
    suspend fun getSavedCities(): List<CityPojo>
    suspend fun removeCity(city: CityPojo)
    suspend fun insertCity(city: CityPojo)
}