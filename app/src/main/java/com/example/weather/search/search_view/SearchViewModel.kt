package com.example.weather.search.search_view

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.search.search_repo.SearchApiState
import com.example.weather.search.search_repo.SearchRepoInterface
import com.example.weather.search.search_repo.search_result_pojo.CityPojo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(private var repo: SearchRepoInterface) : ViewModel() {
    private var _postSearchStateFlow = MutableStateFlow<SearchApiState>(SearchApiState.Loading)
    val responseSearchFlow: StateFlow<SearchApiState>
        get() = _postSearchStateFlow

    private fun getSearchData(city: String) {
        _postSearchStateFlow.value = SearchApiState.Loading
        viewModelScope.launch {
            try {
                repo.getSearchResult(city).collect { data ->
                    Log.d("MySearch", data.toString())
                    _postSearchStateFlow.value = SearchApiState.Success(data)
                }
            } catch (e: Exception) {
                _postSearchStateFlow.value = SearchApiState.Failure(e)
            }
        }

    }

    fun search(myQuery: String) {
        viewModelScope.launch {
            getSearchData(myQuery)
        }
    }

    fun saveCity(city: CityPojo) {
        viewModelScope.launch {
            repo.insertCity(city)
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}