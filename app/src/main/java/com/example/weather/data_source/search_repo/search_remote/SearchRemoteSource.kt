package com.example.weather.data_source.search_repo.search_remote

import com.example.weather.data_source.search_repo.search_result_pojo.SearchResponse

interface SearchRemoteSource {
    suspend fun getSearchResponse(city: String): SearchResponse
   // suspend fun getCityByLatLong(lat: Double, long: Double): SearchResponse
}