package com.example.weather.map.repo

import com.example.weather.localSource.LocalSource
import com.example.weather.map.repo.search_remote.SearchRemoteSource
import com.example.weather.map.repo.search_result_pojo.CityPojo
import com.example.weather.map.repo.search_result_pojo.SearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class Repo private constructor(
    var remoteSource: SearchRemoteSource,
    var localSource: LocalSource
) : RepoInterface {

    companion object {
        private var INSTANCE: Repo? = null
        fun getInstance(remoteSource: SearchRemoteSource, localSource: LocalSource): Repo {
            return INSTANCE ?: synchronized(this) {
                val instance = Repo(remoteSource, localSource)
                INSTANCE = instance
                instance
            }
        }
    }

    override suspend fun getSearchResult(city: String): Flow<SearchResponse> {
        return flow {
            emit(remoteSource.getSearchResponse(city))
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun insertCity(city: CityPojo) = localSource.insertCity(city)

}