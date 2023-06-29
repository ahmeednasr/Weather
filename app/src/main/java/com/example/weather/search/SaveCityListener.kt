package com.example.weather.search

import com.example.weather.search.search_repo.search_result_pojo.CityPojo

interface SaveCityListener {
    fun onCityClick(city: CityPojo)
}