package com.example.weather.data_source.location_weather_repo

import com.example.weather.data.source.FakeLocalSource
import com.example.weather.data.source.FakeRemoteDataSource
import com.example.weather.data_source.ConnectionResult
import com.example.weather.data_source.location_weather_repo.current_pojo.CurrentPojo
import com.example.weather.data_source.location_weather_repo.location_weather_pojo.LocationWeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LocationWeatherRepoTest(var remote: FakeRemoteDataSource,var local: FakeLocalSource) :
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

    override suspend fun getCurrentCity(
        latitude: Double,
        longitude: Double,
        language: String
    ): ConnectionResult<CurrentPojo> {
        return ConnectionResult.Success(remote.getCurrentCity(latitude, longitude, language))
    }

    override fun setLocationType(key: String) {
        TODO("Not yet implemented")
    }

    override fun getLocationType(): String {
        return local.getLocationType()
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