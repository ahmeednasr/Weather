package com.example.weather.data.source

import com.example.weather.data_source.ConnectionResult
import com.example.weather.data_source.location_weather_repo.LocationWeatherRepoInterface
import com.example.weather.data_source.location_weather_repo.current_pojo.CurrentPojo
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

    override suspend fun getCurrentCity(
        latitude: Double,
        longitude: Double,
        language: String
    ): ConnectionResult<CurrentPojo> {
        return ConnectionResult.Success(remoteSource.getCurrentCity(latitude, longitude, language))
    }

    override fun setLocationType(key: String) {
        localSource.setLocationType(key)
    }

    override fun getLocationType(): String {
        return localSource.getLocationType()
    }

    override fun getMapDetails(): Pair<Double, Double> {
        return localSource.getMapDetails()
    }

    override fun getSpeedUnit(): String {
        return localSource.getSpeedUnit()
    }

    override fun getTempUnit(): String {
        return localSource.getTempUnit()
    }

    override fun cacheResponse(response: LocationWeatherResponse) {
        TODO("Not yet implemented")
    }

    override fun getCachedResponse(): LocationWeatherResponse {
        TODO("Not yet implemented")
    }
}