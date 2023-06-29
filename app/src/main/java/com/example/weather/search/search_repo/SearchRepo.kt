package com.example.weather.search.search_repo

import com.example.weather.search.search_repo.remote.SearchRemoteSource
import com.example.weather.search.search_repo.search_result_pojo.SearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SearchRepo private constructor(var remoteSource: SearchRemoteSource) : SearchRepoInterface {

    companion object {
        private var INSTANCE: SearchRepo? = null
        fun getInstance(remoteSource: SearchRemoteSource): SearchRepo {
            return INSTANCE ?: synchronized(this) {
                val instance = SearchRepo(remoteSource)
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

}