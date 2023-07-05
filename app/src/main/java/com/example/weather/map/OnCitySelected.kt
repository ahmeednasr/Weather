package com.example.weather.map

import com.example.weather.map.repo.search_result_pojo.CityPojo

interface OnCitySelected {
    fun selectCity(city: CityPojo)
}