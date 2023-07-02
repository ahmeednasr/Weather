package com.example.weather.home.location_weather_view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.companion.MyCompanion
import com.example.weather.databinding.FragmentHomeBinding
import com.example.weather.home.location_weather_repo.LocationWeatherApiState
import com.example.weather.home.location_weather_repo.LocationWeatherRepo
import com.example.weather.home.location_weather_repo.remote.LocationWeatherApiClient

import com.google.android.gms.location.*
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*

import java.text.SimpleDateFormat
import java.util.*
import com.example.weather.R


class LocationWeatherView : Fragment() {
    private lateinit var homeFactory: LocationWeatherFactory
    lateinit var homeViewModel: LocationWeatherViewModel
    lateinit var binding: FragmentHomeBinding
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    lateinit var geocoder: Geocoder
    lateinit var hourlyAdapter: HourlyAdapter
    lateinit var dailyAdapter: DailyAdapter
    lateinit var myContext: Context
    lateinit var sharedPreferences: SharedPreferences
    lateinit var locationToken: String
    var lat: Double = 21.38
    var long: Double = 39.85
    lateinit var speedUnit: String
    lateinit var tempUnit: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        myContext = inflater.context

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("ONMYCREATE", "ONMYCREATE")
        sharedPreferences = myContext.getSharedPreferences("PREFS", 0)
        val editor = sharedPreferences.edit()
        locationToken = sharedPreferences.getString(MyCompanion.LOCATION_KEY, "").toString()
        val speedUnitToken = sharedPreferences.getString(MyCompanion.SPEED_KEY, "")
        if (speedUnitToken != null) {
            speedUnit = when (speedUnitToken) {
                MyCompanion.METER_PER_SECOND -> speedUnitToken
                MyCompanion.MILES_PER_HOUR -> speedUnitToken
                else -> MyCompanion.METER_PER_SECOND
            }
            editor.putString(MyCompanion.SPEED_KEY, speedUnit)

        }
        val tempUnitToken = sharedPreferences.getString(MyCompanion.TEMP_KEY, "")
        if (tempUnitToken != null) {
            tempUnit = when (tempUnitToken) {
                MyCompanion.C -> tempUnitToken
                MyCompanion.F -> tempUnitToken
                else -> MyCompanion.K
            }
            editor.putString(MyCompanion.TEMP_KEY, tempUnit)
        }
        editor.apply()

        hourlyAdapter = HourlyAdapter(requireContext(), tempUnit)
        binding.hourlyRecycler.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.hourlyRecycler.adapter = hourlyAdapter

        dailyAdapter = DailyAdapter(requireContext(), tempUnit)
        binding.dailyRecycler.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.dailyRecycler.isNestedScrollingEnabled
        binding.dailyRecycler.adapter = dailyAdapter

        homeFactory =
            LocationWeatherFactory(LocationWeatherRepo.getInstance(LocationWeatherApiClient.getInstance()))
        homeViewModel = ViewModelProvider(this, homeFactory)[LocationWeatherViewModel::class.java]

        homeViewModel.getTime()
        binding.refresh.setOnRefreshListener {
            //refreshDate()
            homeViewModel.getCurrentLocationResponse(
                lat,
                long,
                Locale.getDefault().language
            )
            Toast.makeText(requireContext(), "refresh", Toast.LENGTH_SHORT).show()
        }
        lifecycleScope.launch {
            homeViewModel.responseFlow.collect { result ->
                when (result) {
                    is LocationWeatherApiState.Loading -> {
                        binding.loading.visibility = View.VISIBLE
                        binding.pageLayout.visibility = View.GONE
                    }
                    is LocationWeatherApiState.Success -> {
                        binding.loading.visibility = View.GONE
                        binding.refresh.isRefreshing = false

                        val current = result.data.current

                        geocoder = Geocoder(myContext, Locale.getDefault())
                        val addressList =
                            geocoder.getFromLocation(result.data.lat, result.data.lon, 1)
                        Log.i("err", "address name: $addressList")
                        if (addressList != null && addressList.isNotEmpty()) {
                            val address = addressList[0]
                            val cityName = address.subAdminArea
                            Log.i("err", "City in name: $cityName")
                            if (cityName != null && cityName.isNotEmpty()) {
                                binding.city.text = cityName
                                Log.i("err", "City in name: $cityName")
                            }
                        }
                        getTime()

                        binding.tempValue.text = MyCompanion.getTemp(tempUnit, current.temp)
                        binding.tempMeasur.text = when (tempUnit) {
                            MyCompanion.C -> context?.getString(R.string.tempunit_celsius)
                            MyCompanion.F -> context?.getString(R.string.tempunit_fahrenheit)
                            MyCompanion.K -> context?.getString(R.string.tempunit_kelvin)
                            else -> null // Handle any other cases if necessary
                        }
                        binding.description.text = current.weather[0].description
                        Picasso.get()
                            .load(MyCompanion.getIconLink(current.weather[0].icon))
                            .into(binding.currentIcon)
                        hourlyAdapter.submitList(result.data.hourly)
                        dailyAdapter.submitList(result.data.daily)

                        binding.pressureValue.text = current.pressure.toString()
                        binding.humidityValue.text = current.humidity.toString()
                        binding.windSpeedValue.text =
                            MyCompanion.getSpeed(speedUnit, current.wind_speed)
                        binding.windSpeed.text = when (speedUnit) {
                            MyCompanion.MILES_PER_HOUR -> context?.getString(R.string.mil_h)
                            else -> context?.getString(R.string.m_s)
                        }
                        val cloud = "${current.clouds}"
                        binding.cloudValue.text = cloud
                        binding.ultraValue.text = current.uvi.toString()
                        binding.visibiltyValue.text = current.visibility.toString()
                        binding.pageLayout.visibility = View.VISIBLE

                    }
                    else -> {
                        binding.loading.visibility = View.VISIBLE
                        binding.pageLayout.visibility = View.GONE
                        Toast.makeText(context, "Error loading", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

        }
    }


    private var mLocationCallBack: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            val mLastLocation: Location? = p0.lastLocation
            if (mLastLocation != null) {
                lat = mLastLocation.latitude
                long = mLastLocation.longitude
                geocoder = Geocoder(myContext)
                var address =
                    geocoder.getFromLocation(mLastLocation.latitude, mLastLocation.longitude, 1)
                if (address != null) {
                    var newLocality = address[0].locality

                    if (MyCompanion.currentCity != newLocality) {
                        MyCompanion.currentCity = newLocality
                        Log.i("city", "old ${MyCompanion.currentCity}")
                        Log.i("city", "new $newLocality")
                        homeViewModel.getCurrentLocationResponse(
                            lat,
                            long,
                            Locale.getDefault().language
                        )
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //step2 call method to get location
        if (locationToken == MyCompanion.GPS) {
            getLastLocation()
        } else if (locationToken == MyCompanion.MAP) {
            //token = sharedPreferences.getString(MyCompanion.LOCATION_KEY, "").toString()
            lat = sharedPreferences.getFloat(MyCompanion.LATITUDE, 0.0f).toDouble()
            long = sharedPreferences.getFloat(MyCompanion.LONGITUDE, 0.0f).toDouble()
            homeViewModel.getCurrentLocationResponse(
                lat,
                long,
                Locale.getDefault().language
            )
        }
    }


    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                requestNewLocationData()
            } else {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        val result = ActivityCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return result
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            5005
        )
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        //base
        val mLocationRequest = LocationRequest()
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        mLocationRequest.setInterval(1000)
        //ref of locations
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(myContext)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest,
            mLocationCallBack,
            Looper.myLooper()
        )
    }

    private fun getTime() {
        lifecycleScope.launch {
            homeViewModel.currentTimeStateFlow.collect { time ->
                val dateFormat = SimpleDateFormat("EEE,dd MMMM", Locale.getDefault())
                val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
                binding.date.text = dateFormat.format(time)
                binding.time.text = timeFormat.format(time)

            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

    }
//
//    fun get(){
//        lifecycleScope.launch {
//            MyCompanion.locationState.collect{
//
//            }
//        }
//
//    }
}