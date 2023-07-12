package com.example.weather.data_source.alert_repo

import com.example.weather.data_source.ConnectionResult
import com.example.weather.data_source.alert_repo.alert_pojo.AlertResponse
import com.example.weather.data_source.alert_repo.alert_pojo.SavedAlert
import kotlinx.coroutines.flow.Flow

interface AlertRepoInterface {
    suspend fun getRemoteAlerts(
        latitude: Double,
        longitude: Double,
    ): Flow<AlertResponse>

    suspend fun getSavedAlerts(): List<SavedAlert>
    suspend fun insertAlert(alert: SavedAlert)
    suspend fun removeAlert(alert: SavedAlert)
}