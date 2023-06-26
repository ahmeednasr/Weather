package com.example.weather.binding_utils

import android.util.Log
import androidx.databinding.InverseMethod
import java.text.DecimalFormat

object MyBindingUtils {
    @InverseMethod("convertDoubleToString")
    fun convertStringToDouble(value:String):Double{
        return try {
            value.toDouble()
        }catch (e:java.lang.NumberFormatException){
            -1.0
        }
    }
    fun convertDoubleToString(value:Double):String{
        return value.toString()
    }
    @InverseMethod("convertIntToString")
    fun convertStringToInt(value:String):Int{
        return try {
            value.toInt()
        }catch (e:java.lang.NumberFormatException){
            -1
        }
    }
    fun convertIntToString(value:Int):String{
        return value.toString()
    }
}