package com.example.weather.data.source

import com.example.weather.data_source.localSource.LocalSource
import com.example.weather.data_source.location_weather_repo.location_weather_pojo.LocationWeatherResponse
import com.example.weather.data_source.search_repo.search_result_pojo.CityPojo
import com.example.weather.system.companion.MyCompanion

class FakeLocalSource(
    var cities: MutableList<CityPojo> = mutableListOf(),
    var locationKey: String?,
    var lat: Double?,
    var long: Double?,
    private var tempUnitToken: String? = null, private var speedUnitToken: String? = null
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
            MyCompanion.GPS -> locationKey!!
            MyCompanion.MAP -> locationKey!!
            else -> {
                default
            }
        }
    }

    override fun setLocationType(key: String) {
        locationKey = key
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
        var speedUnit = ""
        val default = MyCompanion.METER_PER_SECOND
        speedUnit = when (speedUnitToken) {
            MyCompanion.METER_PER_SECOND -> speedUnitToken!!
            MyCompanion.MILES_PER_HOUR -> speedUnitToken!!
            else -> {
                default
            }
        }
        return speedUnit
    }

    override fun getTempUnit(): String {
        var tempUnit = ""
        val default = MyCompanion.K
        tempUnit = when (tempUnitToken) {
            MyCompanion.C -> tempUnitToken!!
            MyCompanion.F -> tempUnitToken!!
            else -> {
                default
            }
        }
        return tempUnit
    }

    override fun cacheResponse(response: LocationWeatherResponse) {
        TODO("Not yet implemented")
    }

    override fun getCachedResponse(): LocationWeatherResponse? {
        TODO("Not yet implemented")
    }
}