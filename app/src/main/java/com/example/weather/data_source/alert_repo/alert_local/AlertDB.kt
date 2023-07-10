package com.example.weather.data_source.alert_repo.alert_local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weather.data_source.alert_repo.alert_pojo.SavedAlert

@Database(entities = [SavedAlert::class], version = 1)
abstract class AlertDB : RoomDatabase() {
    abstract fun getAlertDao(): AlertDao

    companion object {
        @Volatile
        private var INSTANCE: AlertDB? = null

        fun getInstance(ctx: Context): AlertDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    ctx.applicationContext,
                    AlertDB::class.java,
                    "alert_data_base"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}