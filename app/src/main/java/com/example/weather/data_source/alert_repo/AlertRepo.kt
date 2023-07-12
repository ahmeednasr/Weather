package com.example.weather.data_source.alert_repo

import com.example.weather.data_source.ConnectionResult
import com.example.weather.data_source.alert_repo.alert_local.AlertLocalSourceInterface
import com.example.weather.data_source.alert_repo.alert_pojo.AlertResponse
import com.example.weather.data_source.alert_repo.alert_pojo.SavedAlert
import com.example.weather.data_source.alert_repo.alert_remote.AlertRemoteSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AlertRepo private constructor(
    var local: AlertLocalSourceInterface,
    var remote: AlertRemoteSource
) : AlertRepoInterface {
    companion object {
        private var INSTANCE: AlertRepo? = null
        fun getInstance(local: AlertLocalSourceInterface, remote: AlertRemoteSource): AlertRepo {
            return INSTANCE ?: synchronized(this) {
                val instance = AlertRepo(local, remote)
                INSTANCE = instance
                instance
            }
        }
    }

    override suspend fun getRemoteAlerts(latitude: Double, longitude: Double): Flow<AlertResponse> {
        return flow {
            emit(remote.getAlarms(latitude, longitude))
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getSavedAlerts(): List<SavedAlert> {
        return local.getAlerts()
    }

    override suspend fun insertAlert(alert: SavedAlert) {
        local.insertAlert(alert)
    }

    override suspend fun removeAlert(alert: SavedAlert) {
        local.removeAlert(alert)
    }
}