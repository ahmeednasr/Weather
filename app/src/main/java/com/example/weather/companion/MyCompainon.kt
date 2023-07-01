package com.example.weather.companion

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MyCompanion {
    companion object {
        const val API_Key = "a117466978c46167246dc8d6a700cce6"
        var currentCity = ""
        fun getIconLink(id: String): String {
            return "https://openweathermap.org/img/wn/$id@2x.png"
        }

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

        fun getTemp(unit: String, value: Double): Int {
            val convertedValue = when (unit) {
                C -> value - 273.15
                F -> (value - 273.15) * (9 / 5) + 32
                else -> value
            }
            return convertedValue.toInt()
        }
//       enum class LocationType(val value: String) {
//            GPS("gps"),
//            MAP("map")
//        }
//        enum class UnitsType(val value: String) {
//            METRIC("metric"),
//            IMPERIAL("imperial")
//        }
//        enum class LanguageType(val value: String) {
//            ARABIC("ar"),
//            ENGLISH("en")
//        }
//
//        //var currentUnit=()
//
//            private val _locationType = MutableStateFlow(LocationType.GPS)
//            val locationState: StateFlow<LocationType> = _locationType
//
//            private val _unitsType = MutableStateFlow(UnitsType.METRIC)
//            val unitsState: StateFlow<UnitsType> = _unitsType
//
//            private val _languageType = MutableStateFlow(LanguageType.ARABIC)
//            val languageState: StateFlow<LanguageType> = _languageType
//
//            fun setLocationType(locationType: LocationType) {
//                _locationType.value = locationType
//            }
//
//            fun setUnitsType(unitsType: UnitsType) {
//                _unitsType.value = unitsType
//            }
//
//            fun setLanguageType(languageType: LanguageType) {
//                _languageType.value = languageType
//            }

    }
}