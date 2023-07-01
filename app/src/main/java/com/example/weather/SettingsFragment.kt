package com.example.weather

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.app.ActivityCompat.recreate
import com.example.weather.companion.MyCompanion
import java.util.*

class SettingsFragment : Fragment() {
    lateinit var languageGroup:RadioGroup
    lateinit var arabicRadio:RadioButton
    lateinit var englishRadio:RadioButton

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
        val currentLocale = Locale.getDefault()
        val currentLanguage = currentLocale.language
       //  languageGroup = view.findViewById(R.id.language_group)
        arabicRadio=view.findViewById(R.id.arabic)
        englishRadio=view.findViewById(R.id.english)
        val sharedPreferences = requireContext().getSharedPreferences("PREFS", 0)
        val editor = sharedPreferences.edit()
        if (currentLanguage == "en") {
            englishRadio.isChecked=true
            //languageGroup.check(R.id.english)
        } else if (currentLanguage == "ar") {
            arabicRadio.isChecked=true
           // languageGroup.check(R.id.arabic)
        }
        val languageRadioGroup = view.findViewById<RadioGroup>(R.id.language_group)
        languageRadioGroup.setOnCheckedChangeListener { group, checkedId ->
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
//        languageGroup.setOnCheckedChangeListener { _, checkedId ->
//            when (checkedId) {
//                R.id.arabic -> {
//                    Locale.setDefault(Locale("ar"))
//                    //MyCompanion.setLanguageType(MyCompanion.Companion.LanguageType.ARABIC)
//                    // Handle arabic button selected
//                    requireActivity().recreate()
//                }
//                R.id.english -> {
//                    Locale.setDefault(Locale("en"))
//                    requireActivity().recreate()
//                    //MyCompanion.setLanguageType(MyCompanion.Companion.LanguageType.ENGLISH)
//                    // Handle english button selected
//                }
//            }
//        }
    }

}