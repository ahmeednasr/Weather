package com.example.weather.data_source.alert_repo.alert_pojo

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Dao
@Entity(tableName = "saved_alerts")
data class SavedAlert(
    @PrimaryKey
    var workerId: UUID,
    val cityName: String,
    val lat: Double,
    val lon: Double,
    var startTime: Long,
)