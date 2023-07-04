package com.example.weather.favorite.favorite_repo

import androidx.lifecycle.LiveData
import com.example.weather.localSource.LocalSource
import com.example.weather.search.search_repo.search_result_pojo.CityPojo
import kotlinx.coroutines.flow.Flow

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
    override suspend fun insertCity(city: CityPojo) {
        localSource.insertCity(city)
    }

}