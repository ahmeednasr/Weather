package com.example.weather.main_activty

sealed class ConnectionState {
    class Success(val data: String) : ConnectionState()
    class Lose(val msg: String) : ConnectionState()
    object Loading : ConnectionState()
}
