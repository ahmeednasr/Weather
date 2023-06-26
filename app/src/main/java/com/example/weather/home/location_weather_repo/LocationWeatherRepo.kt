package com.example.weather.home.location_weather_repo

import android.util.Log
import com.example.weather.home.location_weather_repo.location_weather_pojo.LocationWeatherResponse
import com.example.weather.home.location_weather_repo.remote.LocationWeatherRemoteSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class LocationWeatherRepo private constructor(var remoteSource: LocationWeatherRemoteSource) :
    LocationWeatherRepoInterface {

    companion object {
        private var INSTANCE: LocationWeatherRepo? = null
        fun getInstance(remoteSource: LocationWeatherRemoteSource): LocationWeatherRepo {
            return INSTANCE ?: synchronized(this) {
                val instance = LocationWeatherRepo(remoteSource)
                INSTANCE = instance
                Log.i("MYTAG", "getInstance in HomeRepo")
                instance
            }
        }
    }

    override suspend fun getCurrentLocationResponse(
        latitude: Double,
        longitude: Double,
        units: String,
        language: String,
    ): Flow<LocationWeatherResponse> {
        var res =
            remoteSource.getCurrentLocationResponse(latitude, longitude, units, language)
        res.hourly=res.hourly.subList(0,25)
        res.daily=res.daily.subList(1,7)
        Log.i("MYTAG", "getCurrentLocationResponse in HomeRepo")
        return flow {
            Log.i("MYTAG", "flow in getCurrentLocationResponse in HomeRepo")
            emit(res)
        }.flowOn(Dispatchers.IO)
    }
}