package com.example.weather.favorite.favorite_repo

import com.example.weather.map.repo.search_result_pojo.CityPojo

interface FavoriteRepoInterface {
    suspend fun getSavedCities(): List<CityPojo>
    suspend fun removeCity(city: CityPojo)
    suspend fun insertCity(city: CityPojo)
}