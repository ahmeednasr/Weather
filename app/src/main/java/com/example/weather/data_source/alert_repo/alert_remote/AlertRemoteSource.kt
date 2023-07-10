package com.example.weather.data_source.alert_repo.alert_remote

import com.example.weather.data_source.alert_repo.alert_pojo.AlertResponse

interface AlertRemoteSource {
        suspend fun getAlarms(
            latitude: Double,
            longitude: Double,
        ): AlertResponse
}