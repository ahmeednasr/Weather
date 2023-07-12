package com.example.weather.data.source

import com.example.weather.data_source.location_weather_repo.LocationWeatherRepoInterface
import com.example.weather.data_source.location_weather_repo.location_weather_pojo.LocationWeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FakeLocationWeatherRepo(
    val remoteSource: FakeRemoteDataSource,
    val localSource: FakeLocalSource
) : LocationWeatherRepoInterface {

    override suspend fun getCurrentLocationResponse(
        latitude: Double,
        longitude: Double,
        language: String
    ): Flow<LocationWeatherResponse> {
        val response = remoteSource.getCurrentLocationResponse(latitude, longitude, language)
        return flow {
            emit(response)
        }.flowOn(Dispatchers.IO)
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