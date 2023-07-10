package com.example.weather.data_source.alert_repo.alert_local

import com.example.weather.data_source.alert_repo.alert_pojo.SavedAlert

interface AlertLocalSourceInterface {
    suspend fun getAlerts(): List<SavedAlert>
    suspend fun insertAlert(alert: SavedAlert)
    suspend fun removeAlert(alert: SavedAlert)
}