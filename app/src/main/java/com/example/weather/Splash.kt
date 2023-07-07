package com.example.weather

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.weather.main_activity.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
        lifecycleScope.launch {
            delay(2000)
            startActivity(Intent(this@Splash, MainActivity::class.java))
            finish()
        }
    }
}