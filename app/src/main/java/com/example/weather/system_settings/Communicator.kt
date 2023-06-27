package com.example.weather.system_settings

interface Communicator {
    fun move()
    fun setlocation(lat:Double,long:Double)
}