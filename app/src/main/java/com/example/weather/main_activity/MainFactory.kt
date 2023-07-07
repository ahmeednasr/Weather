package com.example.weather.main_activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            MainViewModel() as T
        } else {
            throw IllegalArgumentException("ViewModel Class not found")
        }

    }
}