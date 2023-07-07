package com.example.weather.data_source.localSource

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.weather.system.companion.MyCompanion
import com.example.weather.data_source.search_repo.search_result_pojo.CityPojo

class ConcretLocalSource(private val context: Context) : LocalSource {
    private var dao: CityDao
    private var sharedPreferences: SharedPreferences
    private var editor: SharedPreferences.Editor

    init {
        var db = CityDB.getInstance(context)
        dao = db.getCityDao()
        Log.i("TAG", "getallstored in Constractor localsource");
        sharedPreferences = context.getSharedPreferences("PREFS", 0)
        editor = sharedPreferences.edit()
    }

    companion object {
        private var Instance: ConcretLocalSource? = null
        fun getInstance(context: Context): ConcretLocalSource {
            return Instance ?: synchronized(this) {
                val tmp = ConcretLocalSource(context)
                tmp
            }
        }
    }

    override suspend fun getLocalCities(): List<CityPojo> = dao.getCities()

    override suspend fun insertCity(city: CityPojo) {
        city.state = city.state ?: ""
        dao.insertCity(city)
    }

    override suspend fun removeCity(city: CityPojo) = dao.deleteCity(city)


    override fun getLocationType(): String {
        val locationKey = sharedPreferences.getString(MyCompanion.LOCATION_KEY, "")
        var locationState = ""
        val default = MyCompanion.GPS
        if (locationKey != null) {
            locationState = when (locationKey) {
                MyCompanion.GPS -> locationKey
                MyCompanion.MAP -> locationKey
                else -> {
                    editor.putString(MyCompanion.LOCATION_KEY, default)
                    editor.apply()
                    default
                }
            }

        }
        return locationState
    }

    override fun getMapDetails(): Pair<Double, Double> {
        val lat = sharedPreferences.getFloat(MyCompanion.LATITUDE, 0.0f).toDouble()
        val long = sharedPreferences.getFloat(MyCompanion.LONGITUDE, 0.0f).toDouble()
        return Pair(lat, long)
    }


    override fun getSpeedUnit(): String {
        val speedUnitToken = sharedPreferences.getString(MyCompanion.SPEED_KEY, "")
        var speedUnit = ""
        val default = MyCompanion.METER_PER_SECOND
        if (speedUnitToken != null) {

            speedUnit = when (speedUnitToken) {
                MyCompanion.METER_PER_SECOND -> speedUnitToken
                MyCompanion.MILES_PER_HOUR -> speedUnitToken
                else -> {
                    editor.putString(MyCompanion.SPEED_KEY, default)
                    editor.apply()
                    default
                }
            }
        }
        return speedUnit
    }

    override fun getTempUnit(): String {
        val tempUnitToken = sharedPreferences.getString(MyCompanion.TEMP_KEY, "")
        var tempUnit = ""
        val default = MyCompanion.K

        if (tempUnitToken != null) {
            tempUnit = when (tempUnitToken) {
                MyCompanion.C -> tempUnitToken
                MyCompanion.F -> tempUnitToken
                else -> {
                    editor.putString(MyCompanion.TEMP_KEY, default)
                    editor.apply()
                    MyCompanion.K
                }
            }
        }
        return tempUnit
    }
}