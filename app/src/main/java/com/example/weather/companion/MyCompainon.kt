package com.example.weather.companion

class MyCompanion {
    companion object {
        const val API_Key = "a117466978c46167246dc8d6a700cce6"
        fun getIconLink(id: String): String {
            return "https://openweathermap.org/img/wn/$id@2x.png"
        }

        var currentCity = ""
        const val LOCATION_KEY = "LOCATION"
        const val MAP = "MAP"
        const val GPS = "GPS"

        const val LATITUDE = "LAT"
        const val LONGITUDE = "LONG"

        const val LANGUAGE_KEY = "LANGUAGE"
        const val ARABIC = "ar"
        const val ENGLISH = "en"

        const val TEMP_KEY = "TEMP"
        const val K: String = "°K"
        const val C: String = "°C"
        const val F: String = "°F"
        fun getTemp(unit: String, K: Double): String {
            val convertedValue = when (unit) {
                C -> K - 273.15
                F -> (K - 273.15) * (9 / 5) + 32
                else -> K
            }
            return convertedValue.toInt().toString()
        }

        const val SPEED_KEY = "SPEED"
        const val METER_PER_SECOND = "m/s"
        const val MILES_PER_HOUR = "mil/h"
        fun getSpeed(unit: String, speed: Double): String {
            val convertedValue = when (unit) {
                MILES_PER_HOUR -> speed * 2.23694
                else -> speed
            }
            return  String.format("%.2f", convertedValue)
        }

        const val FAV_FRAGMENT = "FAV_FRAGMENT"
        const val SETTINGS_FRAGMENT = "SETTINGS_FRAGMENT"
    }
}