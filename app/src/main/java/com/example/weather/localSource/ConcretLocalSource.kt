package com.example.weather.localSource

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.weather.companion.MyCompanion
import com.example.weather.search.search_repo.search_result_pojo.CityPojo
import kotlinx.coroutines.flow.Flow
import java.util.*

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

    override suspend fun insertCity(city: CityPojo) = dao.insertCity(city)

    override suspend fun removeCity(city: CityPojo) = dao.deleteCity(city)

    override fun getLocationType(): String {
        return sharedPreferences.getString(MyCompanion.LOCATION_KEY, "").toString()
    }

    override fun getMapDetails(): Pair<Double, Double> {
        val lat = sharedPreferences.getFloat(MyCompanion.LATITUDE, 0.0f).toDouble()
        val long = sharedPreferences.getFloat(MyCompanion.LONGITUDE, 0.0f).toDouble()
        return Pair(lat, long)
    }


    override fun getSpeedUnit(): String {
        val speedUnitToken = sharedPreferences.getString(MyCompanion.SPEED_KEY, "")
        var speedUnit: String = ""
        if (speedUnitToken != null) {

            speedUnit = when (speedUnitToken) {
                MyCompanion.METER_PER_SECOND -> speedUnitToken
                MyCompanion.MILES_PER_HOUR -> speedUnitToken
                else -> MyCompanion.METER_PER_SECOND
            }
            editor.putString(MyCompanion.SPEED_KEY, speedUnit)
            editor.apply()
        }
        return speedUnit
    }

    override fun getTempUnit(): String {
        val tempUnitToken = sharedPreferences.getString(MyCompanion.TEMP_KEY, "")
        var tempUnit: String = ""
        if (tempUnitToken != null) {
            tempUnit = when (tempUnitToken) {
                MyCompanion.C -> tempUnitToken
                MyCompanion.F -> tempUnitToken
                else -> MyCompanion.K
            }
            editor.putString(MyCompanion.TEMP_KEY, tempUnit)
            editor.apply()
        }
        return tempUnit
    }
//    fun getLanguage():String{
//        return Locale.getDefault().language
//    }
}