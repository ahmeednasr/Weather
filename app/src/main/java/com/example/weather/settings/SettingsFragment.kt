package com.example.weather.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.weather.R
import com.example.weather.companion.MyCompanion
import com.example.weather.favorite.favorite_view.FavoriteViewDirections
import java.util.*

class SettingsFragment : Fragment() {
    lateinit var controller: NavController

    lateinit var languageRadioGroup: RadioGroup
    lateinit var arabicRadio: RadioButton
    lateinit var englishRadio: RadioButton

    lateinit var locationTypeRadioGroup: RadioGroup
    lateinit var gpsRadio: RadioButton
    lateinit var mapRadio: RadioButton

    lateinit var tempUnitRadioGroup: RadioGroup
    lateinit var cRadio: RadioButton
    lateinit var fRadio: RadioButton
    lateinit var kRadio: RadioButton

    lateinit var speedUnitRadioGroup: RadioGroup
    lateinit var meterPerSecond: RadioButton
    lateinit var milePerHour: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI(view)

        controller = Navigation.findNavController(view)

        val sharedPreferences = requireContext().getSharedPreferences("PREFS", 0)
        val editor = sharedPreferences.edit()
        val locationToken = sharedPreferences.getString(MyCompanion.LOCATION_KEY, "")
        val speedUnitToken = sharedPreferences.getString(MyCompanion.SPEED_KEY, "")
        val tempUnitToken = sharedPreferences.getString(MyCompanion.TEMP_KEY, "")
        when (locationToken) {
            MyCompanion.GPS -> {
                gpsRadio.isChecked = true
            }
            MyCompanion.MAP -> {
                mapRadio.isChecked = true
            }
        }
        //  editor.putString(MyCompanion.SPEED_KEY, speedUnit)

        locationTypeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.gps) {
                editor.putString(MyCompanion.LOCATION_KEY, MyCompanion.GPS)
                editor.apply()
            } else if (checkedId == R.id.map) {
                //set direction to map and in map send flag from setting to map to handel it
                controller.navigate(
                    SettingsFragmentDirections.actionSettingsFragmentToMyMapFragment(MyCompanion.SETTINGS_FRAGMENT)
                )
                //editor.putString(MyCompanion.LOCATION_KEY, MyCompanion.MAP)
            }
        }
        when (speedUnitToken) {
            MyCompanion.METER_PER_SECOND -> {
                meterPerSecond.isChecked = true
            }
            MyCompanion.MILES_PER_HOUR -> {
                milePerHour.isChecked = true
            }
        }
        speedUnitRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.meter_s) {
                editor.putString(MyCompanion.SPEED_KEY, MyCompanion.METER_PER_SECOND)
            } else if (checkedId == R.id.mile_h) {
                editor.putString(MyCompanion.SPEED_KEY, MyCompanion.MILES_PER_HOUR)
            }
            editor.apply()
        }
        when (tempUnitToken) {
            MyCompanion.K -> {
                kRadio.isChecked = true
            }
            MyCompanion.C -> {
                cRadio.isChecked = true
            }
            MyCompanion.F -> {
                fRadio.isChecked = true
            }
        }
        tempUnitRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.C -> {
                    editor.putString(MyCompanion.TEMP_KEY, MyCompanion.C)
                }
                R.id.F -> {
                    editor.putString(MyCompanion.TEMP_KEY, MyCompanion.F)
                }
                R.id.K -> {
                    editor.putString(MyCompanion.TEMP_KEY, MyCompanion.K)
                }
            }
            editor.apply()
        }
        val currentLocale = Locale.getDefault()
        val currentLanguage = currentLocale.language
        if (currentLanguage == "en") {
            englishRadio.isChecked = true
            //languageGroup.check(R.id.english)
        } else if (currentLanguage == "ar") {
            arabicRadio.isChecked = true
            // languageGroup.check(R.id.arabic)
        }

        languageRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.english) {
                // Change language to English
                Locale.setDefault(Locale("en"))
                editor.putString(MyCompanion.LANGUAGE_KEY, MyCompanion.ENGLISH)
            } else if (checkedId == R.id.arabic) {
                // Change language to Arabic
                Locale.setDefault(Locale("ar"))
                editor.putString(MyCompanion.LANGUAGE_KEY, MyCompanion.ARABIC)
            }
            editor.apply()
            requireActivity().recreate()
        }

    }

    private fun initUI(view: View) {
        languageRadioGroup = view.findViewById<RadioGroup>(R.id.language_group)
        arabicRadio = view.findViewById(R.id.arabic)
        englishRadio = view.findViewById(R.id.english)

        locationTypeRadioGroup = view.findViewById<RadioGroup>(R.id.location_group)
        gpsRadio = view.findViewById(R.id.gps)
        mapRadio = view.findViewById(R.id.map)

        tempUnitRadioGroup = view.findViewById<RadioGroup>(R.id.temp_group)
        cRadio = view.findViewById(R.id.C)
        fRadio = view.findViewById(R.id.F)
        kRadio = view.findViewById(R.id.K)

        speedUnitRadioGroup = view.findViewById<RadioGroup>(R.id.speed_group)
        meterPerSecond = view.findViewById(R.id.meter_s)
        milePerHour = view.findViewById(R.id.mile_h)
    }
}