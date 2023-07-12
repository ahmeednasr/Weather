package com.example.weather.data_source

import com.example.weather.data_source.ConnectionResult.Success

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */

sealed class ConnectionResult<out R> {

    data class Success<out T>(val data: T) : ConnectionResult<T>()
    data class Error(val exception: Exception) : ConnectionResult<Nothing>()
    object Loading : ConnectionResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            Loading -> "Loading"
        }
    }
}

/**
 * `true` if [Result] is of type [Success] & holds non-null [Success.data].
 */
val ConnectionResult<*>.succeeded
    get() = this is Success && data != null
