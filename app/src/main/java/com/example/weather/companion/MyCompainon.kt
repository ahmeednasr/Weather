package com.example.weather.companion

class MyCompanion {
    companion object {
        fun getIconLink(id: String): String {
            return "https://openweathermap.org/img/wn/$id@2x.png"
        }
        var language="ar"
        var currentCity=""
        //var currentUnit=()
    }
}