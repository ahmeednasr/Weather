package com.example.weather

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.weather.main_activty.MainActivity


class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            kotlin.run {
                val intent= Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
//            val sharedPreferences = getSharedPreferences("PREFS", 0)
//            val token = sharedPreferences.getString("TOKEN", "")
//            if (!token.isEmpty()) {
//                startActivity(Intent(this, MainNavigation::class.java))
//                finish()
//            } else {
//                startActivity(Intent(this, WelcomeActivity::class.java))
//                finish()
//            }
        }, 3000)
    }
}