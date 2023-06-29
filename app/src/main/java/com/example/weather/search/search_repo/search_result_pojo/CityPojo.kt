package com.example.weather.search.search_repo.search_result_pojo

data class CityPojo(
    val country: String,
    val lat: Double,
    val local_names: LocalNames,
    val lon: Double,
    val name: String,
    val state: String
)