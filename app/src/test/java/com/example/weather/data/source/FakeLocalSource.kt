package com.example.weather.data.source

import com.example.weather.data_source.localSource.LocalSource
import com.example.weather.data_source.location_weather_repo.location_weather_pojo.LocationWeatherResponse
import com.example.weather.data_source.search_repo.search_result_pojo.CityPojo
import com.example.weather.system.companion.MyCompanion

class FakeLocalSource(
    var cities: MutableList<CityPojo> = mutableListOf(),
    var locationKey: String,
    var lat: Double?,
    var long: Double?

) : LocalSource {
    override suspend fun getLocalCities(): List<CityPojo> {
        return cities.toList()
    }

    override suspend fun insertCity(city: CityPojo) {
        cities.add(city)
    }

    override suspend fun removeCity(city: CityPojo) {
        cities.remove(city)
    }

    override fun getLocationType(): String {
        val default = MyCompanion.GPS
        return when (locationKey) {
            MyCompanion.GPS -> locationKey
            MyCompanion.MAP -> locationKey
            else -> {
                default
            }
        }
    }

    override fun setLocationType(type: String) {
        locationKey = type
    }

    override fun getMapDetails(): Pair<Double, Double> {
        val lat = this.lat ?: 0.0
        val long = this.long ?: 0.0
        return Pair(lat, long)
    }

    override fun setMapDetails(lat: Double, long: Double) {
        this.lat = lat
        this.long = long
    }

    override fun getSpeedUnit(): String {
        TODO("Not yet implemented")
    }

    override fun getTempUnit(): String {
        TODO("Not yet implemented")
    }

    override fun cacheResponse(response: LocationWeatherResponse) {
        TODO("Not yet implemented")
    }

    override fun getCachedResponse(): LocationWeatherResponse {
        TODO("Not yet implemented")
    }
}