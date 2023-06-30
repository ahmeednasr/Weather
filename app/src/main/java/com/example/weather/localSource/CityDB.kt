package com.example.weather.localSource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weather.search.search_repo.search_result_pojo.CityPojo

@Database(entities = [CityPojo::class], version = 1)
abstract class CityDB:RoomDatabase(){
    abstract fun getCityDao():CityDao
    companion object{
        @Volatile
        private var INSTANCE: CityDB? = null
        fun getInstance(ctx: Context): CityDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    ctx.applicationContext,
                    CityDB::class.java,
                    "city_data_base"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}