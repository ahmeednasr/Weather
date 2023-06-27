package com.example.weather

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.weather.companion.MyCompanion
import com.example.weather.main_activty.MainActivity
import com.example.weather.system_settings.SetSetting
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
        lifecycleScope.launch {
            delay(2000)
            val sharedPreferences = getSharedPreferences("PREFS", 0)
            val token = sharedPreferences.getString(MyCompanion.LOCATION_KEY, "")
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