package com.example.weather.data_source.alert_repo.alert_local

import android.content.Context
import com.example.weather.data_source.alert_repo.alert_pojo.SavedAlert

class AlertLocalSource(private val context: Context) : AlertLocalSourceInterface {
    private var dao: AlertDao

    init {
        val db = AlertDB.getInstance(context)
        dao = db.getAlertDao()
    }

    companion object {
        private var INSTANCE: AlertLocalSource? = null
        fun getInstance(ctx: Context): AlertLocalSource {
            return INSTANCE ?: synchronized(this) {
                val tmp = AlertLocalSource(ctx)
                tmp
            }
        }
    }

    override suspend fun getAlerts(): List<SavedAlert> = dao.getAllSavedAlerts()

    override suspend fun insertAlert(alert: SavedAlert) {
        dao.insert(alert)
    }

    override suspend fun removeAlert(alert: SavedAlert) {

        dao.delete(alert.workerId)
    }
}