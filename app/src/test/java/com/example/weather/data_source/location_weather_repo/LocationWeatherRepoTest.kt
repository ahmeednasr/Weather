package com.example.weather.data_source.location_weather_repo

import com.example.weather.data.source.FakeLocalSource
import com.example.weather.data.source.FakeRemoteDataSource
import com.example.weather.data_source.location_weather_repo.location_weather_pojo.LocationWeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LocationWeatherRepoTest(var remote: FakeRemoteDataSource, local: FakeLocalSource) :
    LocationWeatherRepoInterface {
    override suspend fun getCurrentLocationResponse(
        latitude: Double,
        longitude: Double,
        language: String
    ): Flow<LocationWeatherResponse> {
        return flow {
            emit(remote.response)
        }
    }

    override fun getLocationType(): String {
        TODO("Not yet implemented")
    }

    override fun getMapDetails(): Pair<Double, Double> {
        TODO("Not yet implemented")
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