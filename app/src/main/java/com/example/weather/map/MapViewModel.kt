package com.example.weather.map

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.map.repo.ApiState
import com.example.weather.map.repo.RepoInterface
import com.example.weather.map.repo.search_result_pojo.CityPojo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MapViewModel(private var repo: RepoInterface) : ViewModel() {
    private var _postSearchStateFlow = MutableStateFlow<ApiState>(ApiState.Loading)
    val responseSearchFlow: StateFlow<ApiState>
        get() = _postSearchStateFlow

    private fun getSearchData(city: String) {
        _postSearchStateFlow.value = ApiState.Loading
        viewModelScope.launch {
            try {
                repo.getSearchResult(city).collect { data ->
                    Log.d("MySearch", data.toString())
                    Log.d("MySearch", data.size.toString())
                    _postSearchStateFlow.value = ApiState.Success(data)
                }
            } catch (e: Exception) {
                _postSearchStateFlow.value = ApiState.Failure(e)
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