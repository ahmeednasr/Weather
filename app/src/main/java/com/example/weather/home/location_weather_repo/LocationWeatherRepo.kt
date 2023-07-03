package com.example.weather.home.location_weather_repo

import android.util.Log
import com.example.weather.home.location_weather_repo.location_weather_pojo.LocationWeatherResponse
import com.example.weather.home.location_weather_repo.remote.LocationWeatherRemoteSource
import com.example.weather.localSource.LocalSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class LocationWeatherRepo private constructor(
    var remoteSource: LocationWeatherRemoteSource,
    var localSource: LocalSource
) :
    LocationWeatherRepoInterface {
    companion object {
        private var INSTANCE: LocationWeatherRepo? = null
        fun getInstance(
            remoteSource: LocationWeatherRemoteSource,
            localSource: LocalSource
        ): LocationWeatherRepo {
            return INSTANCE ?: synchronized(this) {
                val instance = LocationWeatherRepo(remoteSource, localSource)
                INSTANCE = instance
                instance
            }
        }
    }

    override suspend fun getCurrentLocationResponse(
        latitude: Double,
        longitude: Double,
        language: String,
    ): Flow<LocationWeatherResponse> {
        var res =
            remoteSource.getCurrentLocationResponse(latitude, longitude, language)
        res.hourly = res.hourly.subList(0, 25)
        res.daily = res.daily.subList(1, 7)
        return flow {
            emit(res)
        }.flowOn(Dispatchers.IO)
    }

    override fun getLocationType(): String = localSource.getLocationType()

    override fun getMapDetails(): Pair<Double, Double> = localSource.getMapDetails()

    override fun getSpeedUnit(): String = localSource.getSpeedUnit()

    override fun getTempUnit(): String = localSource.getTempUnit()
}