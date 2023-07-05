package com.example.weather.map.repo

import com.example.weather.map.repo.search_result_pojo.SearchResponse

sealed class ApiState {
    class Success(val data: SearchResponse) : ApiState()
    class Failure(val msg: Throwable) : ApiState()
    object Loading : ApiState()
}