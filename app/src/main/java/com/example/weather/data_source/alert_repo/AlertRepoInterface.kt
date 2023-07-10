package com.example.weather.data_source.alert_repo

import com.example.weather.data_source.alert_repo.alert_pojo.AlertResponse
import com.example.weather.data_source.alert_repo.alert_pojo.SavedAlert

interface AlertRepoInterface {
    suspend fun getRemoteAlerts(
        latitude: Double,
        longitude: Double,
    ): AlertResponse

    suspend fun getSavedAlerts(): List<SavedAlert>
    suspend fun insertAlert(alert: SavedAlert)
    suspend fun removeAlert(alert: SavedAlert)
}