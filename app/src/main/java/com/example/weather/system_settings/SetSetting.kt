package com.example.weather.system_settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.lifecycle.lifecycleScope
import com.example.weather.R
import com.example.weather.map.MapActivity
import com.example.weather.companion.MyCompanion
import com.example.weather.companion.MyCompanion.Companion
import com.example.weather.companion.MyCompanion.Companion.languageState
import com.example.weather.companion.MyCompanion.Companion.locationState
import com.example.weather.companion.MyCompanion.Companion.setLanguageType
import com.example.weather.companion.MyCompanion.Companion.setUnitsType
import com.example.weather.main_activty.MainActivity
import kotlinx.coroutines.launch

class SetSetting : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_set_setting)
//        fragment=supportFragmentManager.findFragmentById(R.id.fragment_view) as Fragment
        var locationRadio = MyCompanion.GPS
        val locationGroup: RadioGroup = findViewById(R.id.mlocation_group)
        val unitsGroup: RadioGroup = findViewById(R.id.units_group)
        val languageGroup: RadioGroup = findViewById(R.id.language_group)
        val saveBtn: Button = findViewById(R.id.save)
        val gpsButton: RadioButton = findViewById(R.id.gps)
        val mapButton: RadioButton = findViewById(R.id.map)
        val metricButton: RadioButton = findViewById(R.id.metric)
        val imperialButton: RadioButton = findViewById(R.id.imperial)
        val arabicButton: RadioButton = findViewById(R.id.arabic)
        val englishButton: RadioButton = findViewById(R.id.english)
        gpsButton.isChecked = true
        metricButton.isChecked = true
        arabicButton.isChecked = true
// Set a listener for the RadioGroup views
        locationGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            when (checkedId) {
                R.id.gps -> {
                    //setLocationType(Companion.LocationType.GPS)
                    locationRadio = MyCompanion.GPS
                }
                R.id.map -> {
                    //setLocationType(Companion.LocationType.MAP)
                    locationRadio = MyCompanion.MAP
                    Toast.makeText(this, "map", Toast.LENGTH_SHORT).show()
                }
            }
        }


        unitsGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            when (checkedId) {
                R.id.metric -> {
                    setUnitsType(Companion.UnitsType.METRIC)
                    // Handle metric button selected
                }
                R.id.imperial -> {
                    setUnitsType(Companion.UnitsType.IMPERIAL)
                    // Handle imperial button selected
                }
            }
        }

        languageGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            when (checkedId) {
                R.id.arabic -> {
                    setLanguageType(Companion.LanguageType.ARABIC)
                    // Handle arabic button selected
                }
                R.id.english -> {
                    setLanguageType(Companion.LanguageType.ENGLISH)
                    // Handle english button selected
                }
            }
        }
        saveBtn.setOnClickListener {
            val sharedPreferences = getSharedPreferences("PREFS", 0)
            val editor = sharedPreferences.edit()
            if (locationRadio == MyCompanion.GPS) {
                editor.putString(MyCompanion.LOCATION_KEY, locationRadio)
                editor.apply()
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            } else if (locationRadio == MyCompanion.MAP) {
                val intent = Intent(this, MapActivity::class.java)
                startActivity(intent)
                Toast.makeText(this, "hllow55", Toast.LENGTH_SHORT).show()
            }
//            val editor = getSharedPreferences("my_prefs", Context.MODE_PRIVATE).edit()
//            editor.putString("location_type", locationType.value.name)
//            editor.apply()
        }
        lifecycleScope.launch {
            locationState.collect {
                println(it.value)
            }
        }
        lifecycleScope.launch {
            languageState.collect {
                println(it.value)
            }
        }
    }
}