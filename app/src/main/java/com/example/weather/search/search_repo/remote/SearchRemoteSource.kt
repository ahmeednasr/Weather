package com.example.weather.search.search_repo.remote

import com.example.weather.search.search_repo.search_result_pojo.SearchResponse

interface SearchRemoteSource {
    suspend fun getSearchResponse(city:String):SearchResponse
}