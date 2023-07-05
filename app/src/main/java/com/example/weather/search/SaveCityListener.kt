package com.example.weather.search

import com.example.weather.map.repo.search_result_pojo.CityPojo

interface SaveCityListener {
    fun onCityClick(city: CityPojo)
}