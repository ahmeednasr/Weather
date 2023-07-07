package com.example.weather.data_source.alert_repo.alert_pojo

data class Alert(
    val description: String,
    val end: Long,
    val event: String,
    val sender_name: String,
    val start: Long,
    val tags: List<String>
)