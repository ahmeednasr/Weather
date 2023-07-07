package com.example.weather.main_activity

sealed class ConnectionState {
    class Success(val data: String) : ConnectionState()
    class Lose(val msg: String) : ConnectionState()
    object Loading : ConnectionState()
}
