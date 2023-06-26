package com.example.weather.home.location_weather_view

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
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


class LocationWeatherView : Fragment() {
    private lateinit var homeFactory: LocationWeatherFactory
    lateinit var homeViewModel: LocationWeatherViewModel
    lateinit var binding: FragmentHomeBinding
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    lateinit var geocoder: Geocoder
    lateinit var hourlyAdapter: HourlyAdapter
    lateinit var dailyAdapter: DailyAdapter

    var lat: Double = 24.08
    var long: Double = 32.89

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hourlyAdapter = HourlyAdapter(requireContext())
        binding.hourlyRecycler.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.hourlyRecycler.adapter = hourlyAdapter

        dailyAdapter = DailyAdapter(requireContext())
        binding.dailyRecycler.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.dailyRecycler.adapter = dailyAdapter
        homeFactory =
            LocationWeatherFactory(LocationWeatherRepo.getInstance(LocationWeatherApiClient.getInstance()))
        homeViewModel = ViewModelProvider(this, homeFactory)[LocationWeatherViewModel::class.java]
        //ref of locations
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        homeViewModel.getTime()

        lifecycleScope.launch {

            homeViewModel.responseFlow.collect { result ->
                when (result) {
                    is LocationWeatherApiState.Loading -> {
                        binding.loading.visibility = View.VISIBLE
                        binding.pageLayout.visibility = View.GONE
                    }
                    is LocationWeatherApiState.Success -> {
                        binding.loading.visibility = View.GONE
                        getTime()

                        binding.tempValue.text = result.data.current.temp.toInt().toString()
                        binding.description.text = result.data.current.weather[0].description
                        Picasso.get()
                            .load(MyCompanion.getIconLink(result.data.current.weather[0].icon))
                            .into(binding.currentIcon)
                        hourlyAdapter.submitList(result.data.hourly)

                        dailyAdapter.submitList(result.data.daily)
                        dailyAdapter.notifyDataSetChanged()
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

                geocoder = Geocoder(requireContext())
                var address =
                    geocoder.getFromLocation(mLastLocation.latitude, mLastLocation.longitude, 1)
                if (address != null) {
                    var newLocality = address[0].locality
                    var oldLocality=MyCompanion.currentCity
                    if (oldLocality != newLocality) {
                        oldLocality = newLocality
                        binding.city.text = oldLocality
                        homeViewModel.getCurrentLocationResponse(
                            lat,
                            long,
                            "metric",
                            "ar",
                        )
                    }
                }
            }
        }


    }

    override fun onResume() {
        super.onResume()
        //step2 call method to get location
        getLastLocation()
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
        mLocationRequest.setInterval(0)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
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
}