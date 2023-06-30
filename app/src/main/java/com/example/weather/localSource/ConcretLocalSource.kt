package com.example.weather.localSource

import android.content.Context
import android.util.Log
import com.example.weather.search.search_repo.search_result_pojo.CityPojo
import kotlinx.coroutines.flow.Flow

class ConcretLocalSource(private val context: Context):LocalSource {
    private var dao: CityDao

    init {
        var db = CityDB.getInstance(context)
        dao = db.getCityDao()
        Log.i("TAG", "getallstored in Constractor localsource");
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
    override fun getLocalCities(): Flow<List<CityPojo>> =dao.getCities()

    override suspend fun insertCity(city: CityPojo)=dao.insertCity(city)

    override suspend fun removeCity(city: CityPojo) =dao.deleteCity(city)
}