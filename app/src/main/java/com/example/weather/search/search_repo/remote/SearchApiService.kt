package com.example.weather.search.search_repo.remote

import com.example.weather.companion.MyCompanion
import com.example.weather.search.search_repo.search_result_pojo.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApiService {
    @GET("direct")
    suspend fun getSearchResult(
        @Query("q") query: String,
        @Query("limit") limit: Int = 10,
        @Query("appid") apiKey: String = MyCompanion.API_Key
    ): SearchResponse
}