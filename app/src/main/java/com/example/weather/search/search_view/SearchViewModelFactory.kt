package com.example.weather.search.search_view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weather.search.search_repo.SearchRepoInterface

class SearchViewModelFactory(private val _repo: SearchRepoInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            SearchViewModel(_repo) as T
        } else {
            throw  IllegalArgumentException("ViewModel Class not found")
        }
    }
}