package com.example.weather.search.search_repo.remote

import android.util.Log
import com.example.weather.search.search_repo.search_result_pojo.SearchResponse
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class SearchApiClient : SearchRemoteSource {
    init {
        val gson: Gson = Gson()
        retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BASE_URL)
            .build()
            .create(SearchApiService::class.java)
    }

    companion object {
        private const val BASE_URL = "https://api.openweathermap.org/geo/1.0/"
        private lateinit var retrofit: SearchApiService
        private var apiClient: SearchApiClient? = null
        fun getInstance(): SearchApiClient {
            return apiClient ?: synchronized(this) {
                val tmp = SearchApiClient()
                apiClient = tmp
                tmp
            }
        }
    }

    override suspend fun getSearchResponse(city: String): SearchResponse {
        var res=retrofit.getSearchResult(city)
        Log.i("Search", res.toString())
        return res
    }
}