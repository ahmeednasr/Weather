package com.example.weather.data_source.alert_repo.alert_local

import androidx.room.*
import com.example.weather.data_source.alert_repo.alert_pojo.SavedAlert

@Dao
interface AlertDao {
    @Query("SELECT * FROM saved_alerts")
    fun getAllSavedAlerts(): List<SavedAlert>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(savedAlert: SavedAlert)

    @Delete
    fun delete(savedAlert: SavedAlert)


}