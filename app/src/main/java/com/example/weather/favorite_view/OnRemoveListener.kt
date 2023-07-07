package com.example.weather.favorite_view

import com.example.weather.data_source.search_repo.search_result_pojo.CityPojo

interface OnCityClickListener {
    fun removeCity(city: CityPojo)
    fun viewCity(city: CityPojo)
}