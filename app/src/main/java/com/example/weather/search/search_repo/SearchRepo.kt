package com.example.weather.search.search_repo

import com.example.weather.localSource.LocalSource
import com.example.weather.search.search_repo.remote.SearchRemoteSource
import com.example.weather.search.search_repo.search_result_pojo.CityPojo
import com.example.weather.search.search_repo.search_result_pojo.SearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SearchRepo private constructor(
    var remoteSource: SearchRemoteSource,
    var localSource: LocalSource
) : SearchRepoInterface {

    companion object {
        private var INSTANCE: SearchRepo? = null
        fun getInstance(remoteSource: SearchRemoteSource, localSource: LocalSource): SearchRepo {
            return INSTANCE ?: synchronized(this) {
                val instance = SearchRepo(remoteSource, localSource)
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