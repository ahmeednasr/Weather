package com.example.weather.companion

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import java.util.*

class MyApplication : Application() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        val sharedPreferences = getSharedPreferences("PREFS", 0)
        val language = sharedPreferences.getString(MyCompanion.LANGUAGE_KEY, "")

        // Update the default locale of the app context
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources = resources
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val sharedPreferences = getSharedPreferences("PREFS", 0)
        val language = sharedPreferences.getString(MyCompanion.LANGUAGE_KEY, "")

        // Update the default locale of the app context
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources = resources
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}