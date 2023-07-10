package com.example.weather.alerts_view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.work.WorkRequest
import com.example.weather.R
import com.example.weather.databinding.AlertPopupBinding
import com.example.weather.databinding.FragmentWeatherAlertsBinding
import com.example.weather.settings.SettingsFragmentDirections
import com.example.weather.system.companion.MyCompanion
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.*


class WeatherAlertsFragment : Fragment() {
    private lateinit var binding: FragmentWeatherAlertsBinding
    var fromTime: Long = 0L
    var toTime: Long = 0L
    var latitude: Double? = null
    var longitude: Double? = null
    lateinit var ctx: Context
    var notifyType = MyCompanion.ALARM
    lateinit var controller: NavController
    lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentWeatherAlertsBinding.inflate(inflater, container, false)
        ctx = inflater.context

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controller = Navigation.findNavController(view)

        binding.addAlertFloating.setOnClickListener {
            val action =
                WeatherAlertsFragmentDirections.actionWeatherAlertsFragmentToMyMapFragment(
                    MyCompanion.ALERTS_FRAGMENT
                )
            controller.navigate(action)
        }
        setFragmentResultListener(MyCompanion.MAP) { _, result ->
            latitude = result.getDouble("latitude")
            longitude = result.getDouble("longitude")
            Log.i("MAP", "lat: $latitude , long: $longitude")
            showDialog()
        }
    }

    private fun showDialog() {
        var popUpBinding = AlertPopupBinding.inflate(layoutInflater)

        fromTime = System.currentTimeMillis()
        Log.d("TAG", "from in showDialog: $fromTime")

        val dateFormat = SimpleDateFormat("E d, MMM", Locale.getDefault())
        val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        popUpBinding.dateFrom.text = dateFormat.format(fromTime)
        popUpBinding.hourFrom.text = timeFormat.format(fromTime)

        popUpBinding.fromBtn.setOnClickListener {
            getTimeStamp { it ->
                Log.d("TAG", "from: $fromTime")
                Log.d("TAG", "it: $it")
                if (timeComparison(fromTime, it)) {
                    fromTime = it
                    Log.d("TAG", "from timestamp: $fromTime")
                    popUpBinding.dateFrom.text = dateFormat.format(it)
                    popUpBinding.hourFrom.text = timeFormat.format(it)
                } else {
                    Toast.makeText(ctx, "you cant select time before now", Toast.LENGTH_SHORT)
                        .show()
                }

            }

        }
        popUpBinding.toBtn.setOnClickListener {
            getTimeStamp { it ->
                if (timeComparison(fromTime, it)) {
                    toTime = it
                    Log.d("TAG", "to timestamp: $toTime")
                    popUpBinding.dateTo.text = dateFormat.format(it)
                    popUpBinding.hourTo.text = timeFormat.format(it)

                } else {
                    Toast.makeText(
                        ctx,
                        "you cant select time before start time",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

            }
        }
        popUpBinding.notifiRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.alarm_radio -> {
                    notifyType = MyCompanion.ALARM
                }
                R.id.notification_radio -> {
                    notifyType = MyCompanion.NOTIFICATION
                }
            }
        }
        popUpBinding.addBtn.setOnClickListener {

            if (timeComparison(fromTime, toTime)) {
                Toast.makeText(
                    ctx,
                    "f: $fromTime t: $toTime lat: $latitude long: $longitude",
                    Toast.LENGTH_SHORT
                )
                    .show()
                dialog.dismiss()
            } else {
                Toast.makeText(
                    ctx,
                    "you don't choose one of times",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
        dialog = MaterialAlertDialogBuilder(ctx)
            .setView(popUpBinding.root)
            .setBackground(
                ContextCompat.getDrawable(
                    ctx,
                    R.drawable.action_bar_drawable
                )
            ).create()
        dialog.show()
    }

    private fun getTimeStamp(setTime: (timestamp: Long) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val minTime = calendar.timeInMillis
        var td: Long
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                // Do something with the selected date
                val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    // Do something with the selected time
                    val selectedCalendar = Calendar.getInstance()
                    selectedCalendar.set(year, monthOfYear, dayOfMonth, hourOfDay, minute)
                    td = selectedCalendar.timeInMillis
                    Log.d("TAG", "Selected timestamp: $td")
                    setTime(td)
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
        datePickerDialog.show()
    }

    private fun timeComparison(oldTime: Long, newTime: Long): Boolean {
        val calendar1 = Calendar.getInstance()
        calendar1.timeInMillis = newTime

        val calendar2 = Calendar.getInstance()
        calendar2.timeInMillis = oldTime

        return calendar1.timeInMillis > calendar2.timeInMillis
    }
}