package com.example.weather.views.alerts_view

import com.example.weather.data_source.alert_repo.alert_pojo.SavedAlert

interface OnAlertRemoveListener {
    fun onRemove(alert: SavedAlert)
}