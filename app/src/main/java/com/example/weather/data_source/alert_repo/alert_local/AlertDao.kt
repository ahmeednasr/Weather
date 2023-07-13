package com.example.weather.data_source.alert_repo.alert_local

import androidx.room.*
import com.example.weather.data_source.alert_repo.alert_pojo.SavedAlert
import java.util.*

@Dao
interface AlertDao {
    @Query("SELECT * FROM saved_alerts")
    suspend fun getAllSavedAlerts(): List<SavedAlert>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(savedAlert: SavedAlert)

    @Query("DELETE FROM saved_alerts WHERE workerId = :uuid")
    suspend fun delete(uuid: UUID)

}