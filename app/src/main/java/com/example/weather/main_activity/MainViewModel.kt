package com.example.weather.main_activity

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel:ViewModel() {
    private var _postStateFlow =
        MutableStateFlow<ConnectionState>(ConnectionState.Loading)
    val connectionFlow: StateFlow<ConnectionState>
    get() = _postStateFlow
        fun setConnectionState(state:ConnectionState){
            _postStateFlow.value=state
        }
    }