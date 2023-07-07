package com.example.weather.data_source.localSource

import androidx.room.*
import com.example.weather.data_source.search_repo.search_result_pojo.CityPojo
@Dao
interface CityDao {
    @Query("SELECT * FROM Cities")
    suspend fun getCities(): List<CityPojo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: CityPojo)

    @Delete
    suspend fun deleteCity(city: CityPojo)
}