package com.example.weather.search.search_repo

import com.example.weather.search.search_repo.search_result_pojo.SearchResponse

sealed class SearchApiState {
    class Success(val data: SearchResponse) : SearchApiState()
    class Failure(val msg: Throwable) : SearchApiState()
    object Loading : SearchApiState()
}