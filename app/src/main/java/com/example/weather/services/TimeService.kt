package com.example.weather.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import java.text.SimpleDateFormat
import java.util.*

//1->create class extend from Service()
class TimeService : Service() {
    //4 create object from inner class MyLocalBinder
    private val myBinder: IBinder = MyLocalBinder()

    //5 return ref of MyLocalBinder obj
    override fun onBind(intent: Intent): IBinder {
        return myBinder
    }

    //3
    fun getCurrentTime(): String {
//        val arabicLocale = Locale("ar")
//        val dateFormat = SimpleDateFormat("dd',' MMMM", arabicLocale)
//        val timeFormat = SimpleDateFormat("h:mm a", arabicLocale)

        //val dateFormat = SimpleDateFormat("EEEE, dd MMMM", Locale.getDefault())
        val dateFormat = SimpleDateFormat("HH:mm:ss MM/dd/yyyy", Locale.US)
        return dateFormat.format(Date())
    }

    //2 create inner class extend from Binder()
    inner class MyLocalBinder : Binder() {
        //2.1 create method return
        fun getService(): TimeService {
            return this@TimeService
        }
    }
}