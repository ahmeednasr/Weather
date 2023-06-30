package com.example.weather.favorite.favorite_view

import com.example.weather.search.search_repo.search_result_pojo.CityPojo

interface OnCityClickListener {
    fun removeCity(city:CityPojo)
    fun viewCity(city:CityPojo)
}