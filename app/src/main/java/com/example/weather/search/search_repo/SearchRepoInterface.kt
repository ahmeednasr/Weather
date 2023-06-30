package com.example.weather.search.search_repo

import com.example.weather.search.search_repo.search_result_pojo.CityPojo
import com.example.weather.search.search_repo.search_result_pojo.SearchResponse
import kotlinx.coroutines.flow.Flow

interface SearchRepoInterface {
    suspend fun getSearchResult(city:String): Flow<SearchResponse>
    suspend fun insertCity(city: CityPojo)
}