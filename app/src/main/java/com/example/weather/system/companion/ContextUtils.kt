package com.example.weather.system.companion

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import java.util.*

class ContextUtils {
    companion object {
        fun updateLocale(context: Context, locale: Locale): ContextWrapper {
            val config = context.resources.configuration
            config.setLocale(locale)
            val newContext = context.createConfigurationContext(config)
            return ContextWrapper(newContext)
        }
    }
}
