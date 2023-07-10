package com.example.weather.data_source.alert_repo.alert_remote

import com.example.weather.data_source.alert_repo.alert_pojo.AlertResponse
import com.example.weather.system.companion.MyCompanion
import retrofit2.http.GET
import retrofit2.http.Query

interface AlertApiService {
    @GET("onecall")
    suspend fun getAlerts(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("exclude") exclude: String = "minutely,hourly,daily,current",
        @Query("appid") apiKey: String = MyCompanion.API_Key
    ): AlertResponse
}