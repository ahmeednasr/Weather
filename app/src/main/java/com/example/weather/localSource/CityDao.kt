package com.example.weather.localSource

import androidx.room.*
import com.example.weather.map.repo.search_result_pojo.CityPojo

@Dao
interface CityDao {
    @Query("SELECT * FROM Cities")
    suspend fun getCities(): List<CityPojo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: CityPojo)

    @Delete
    suspend fun deleteCity(city: CityPojo)
}