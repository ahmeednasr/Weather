package com.example.weather

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.weather.companion.ContextUtils
import com.example.weather.companion.MyCompanion
import com.example.weather.main_activty.MainActivity
import com.example.weather.system_settings.SetSetting
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


class Splash : AppCompatActivity() {
    override fun attachBaseContext(newBase: Context) {
        val localeUpdatedContext: ContextWrapper =
            ContextUtils.updateLocale(newBase, Locale.getDefault())
        super.attachBaseContext(localeUpdatedContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
        val sharedPreferences = getSharedPreferences("PREFS", 0)
        val token = sharedPreferences.getString(MyCompanion.LOCATION_KEY, "")
        val language = sharedPreferences.getString(MyCompanion.LANGUAGE_KEY, "")
        // Update the default locale of the app context
        val locale = Locale(language.toString())
        Locale.setDefault(locale)
        lifecycleScope.launch {
            delay(2000)

            var language: String =
                sharedPreferences.getString(MyCompanion.LANGUAGE_KEY, "").toString()
            Locale.setDefault(Locale(language))
            if (token != null) {
                when {
                    token.isEmpty() -> startActivity(Intent(this@Splash, SetSetting::class.java))
                    else -> startActivity(Intent(this@Splash, MainActivity::class.java))
                }
                finish()
            }
//            val intent = Intent(this@Splash, MainActivity::class.java)
//            startActivity(intent)
//            finish()
        }


    }
}