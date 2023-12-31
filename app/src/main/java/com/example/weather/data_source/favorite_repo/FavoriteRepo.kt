package com.example.weather.data_source.favorite_repo

import com.example.weather.data_source.localSource.LocalSource
import com.example.weather.data_source.search_repo.search_result_pojo.CityPojo

class FavoriteRepo private constructor(var localSource: LocalSource) : FavoriteRepoInterface {
    companion object {
        private var INSTANCE: FavoriteRepo? = null
        fun getInstance(local: LocalSource): FavoriteRepo {
            return INSTANCE ?: synchronized(this) {
                val instance = FavoriteRepo(local)
                INSTANCE = instance
                instance
            }
        }
    }

    override suspend fun getSavedCities(): List<CityPojo> = localSource.getLocalCities()
    override suspend fun removeCity(city: CityPojo) = localSource.removeCity(city)
    override suspend fun insertCity(city: CityPojo) = localSource.insertCity(city)

}