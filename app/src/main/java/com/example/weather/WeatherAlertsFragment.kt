package com.example.weather

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.weather.databinding.AlertPopupBinding
import com.example.weather.databinding.FragmentHomeBinding
import com.example.weather.databinding.FragmentWeatherAlertsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*


class WeatherAlertsFragment : Fragment() {
    private lateinit var binding: FragmentWeatherAlertsBinding
    lateinit var ctx: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentWeatherAlertsBinding.inflate(inflater, container, false)
        ctx = inflater.context
//        return inflater.inflate(R.layout.fragment_weather_alerts, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addAlertFloating.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        var popUpBinding = AlertPopupBinding.inflate(layoutInflater)
        //val dialogView = LayoutInflater.from(ctx).inflate(R.layout.alert_popup, null)

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val minTime = calendar.timeInMillis

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                // Do something with the selected date
                val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    // Do something with the selected time
                }

                val timePickerDialog = TimePickerDialog(
                    context,
                    timeSetListener,
                    hour,
                    minute,
                    false
                )
                timePickerDialog.show()
            }

        val datePickerDialog = DatePickerDialog(
            ctx,
            dateSetListener,
            year,
            month,
            day
        )
        datePickerDialog.datePicker.minDate = minTime

        popUpBinding.fromBtn.setOnClickListener {
            datePickerDialog.show()
        }
        MaterialAlertDialogBuilder(ctx)
            .setView(popUpBinding.root)
            .setBackground(
                ContextCompat.getDrawable(
                    ctx,
                    R.drawable.action_bar_drawable
                )
            )
            .show()
    }

}