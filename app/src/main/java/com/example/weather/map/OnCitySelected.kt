package com.example.weather.map

import com.example.weather.data_source.search_repo.search_result_pojo.CityPojo

interface OnCitySelected {
    fun selectCity(city: CityPojo)
}