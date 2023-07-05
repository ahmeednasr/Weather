package com.example.weather.map.repo

import com.example.weather.map.repo.search_result_pojo.CityPojo
import com.example.weather.map.repo.search_result_pojo.SearchResponse
import kotlinx.coroutines.flow.Flow

interface RepoInterface {
    suspend fun getSearchResult(city: String): Flow<SearchResponse>
    suspend fun insertCity(city: CityPojo)
}