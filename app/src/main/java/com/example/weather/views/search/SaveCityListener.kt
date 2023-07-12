package com.example.weather.views.search

import com.example.weather.data_source.search_repo.search_result_pojo.CityPojo

interface SaveCityListener {
    fun onCityClick(city: CityPojo)
}