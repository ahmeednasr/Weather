package com.example.weather.localSource

import androidx.room.*
import com.example.weather.search.search_repo.search_result_pojo.CityPojo
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {
    @Query("SELECT * FROM Cities")
    fun getCities(): Flow<List<CityPojo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: CityPojo)

    @Delete
    suspend fun deleteCity(city: CityPojo)
}