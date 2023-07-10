package com.example.weather.data_source.alert_repo.alert_pojo

data class AlertResponse(
    val alerts: List<Alert>?,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int
)