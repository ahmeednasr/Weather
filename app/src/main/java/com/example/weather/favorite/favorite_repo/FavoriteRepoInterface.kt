package com.example.weather.favorite.favorite_repo

import androidx.lifecycle.LiveData
import com.example.weather.search.search_repo.search_result_pojo.CityPojo
import kotlinx.coroutines.flow.Flow

interface FavoriteRepoInterface {
    suspend fun getSavedCities(): List<CityPojo>
    suspend fun removeCity(city: CityPojo)
    suspend fun insertCity(city: CityPojo)
}