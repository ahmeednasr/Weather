package com.example.weather.views.city_view

import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.system.companion.MyCompanion
import com.example.weather.databinding.FragmentCityViewBinding
import com.example.weather.home.location_weather_view_model.*
import com.example.weather.data_source.localSource.ConcretLocalSource
import com.example.weather.data_source.location_weather_repo.LocationWeatherApiState
import com.example.weather.data_source.location_weather_repo.LocationWeatherRepo
import com.example.weather.data_source.location_weather_repo.location_weather_remote.LocationWeatherApiClient
import com.example.weather.data_source.search_repo.search_result_pojo.CityPojo
import com.example.weather.views.location_weather_view_model.LocationWeatherFactory
import com.example.weather.views.location_weather_view_model.LocationWeatherViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class CityView : Fragment() {
    private lateinit var cityFactory: LocationWeatherFactory
    lateinit var cityViewModel: LocationWeatherViewModel
    lateinit var binding: FragmentCityViewBinding
    lateinit var hourlyAdapter: HourlyAdapter
    lateinit var dailyAdapter: DailyAdapter
    lateinit var controller: NavController
    lateinit var speedUnit: String
    lateinit var tempUnit: String
    var cityPojo: CityPojo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCityViewBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_city_view, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controller = Navigation.findNavController(view)
        val args = CityViewArgs.fromBundle(requireArguments())
        cityPojo = args.city
        cityFactory = LocationWeatherFactory(
            LocationWeatherRepo.getInstance(
                LocationWeatherApiClient.getInstance(),
                ConcretLocalSource.getInstance(requireContext())
            )
        )
        cityViewModel = ViewModelProvider(this, cityFactory)[LocationWeatherViewModel::class.java]
        //speedUnit=viewModel.getSpeedUnit()
        speedUnit = cityViewModel.getSpeedUnit()
        //tempUnit=viewModel.getTempUnit()
        tempUnit = cityViewModel.getTempUnit()

        hourlyAdapter = HourlyAdapter(requireContext(), tempUnit)
        binding.hourlyRecycler.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.hourlyRecycler.adapter = hourlyAdapter

        dailyAdapter = DailyAdapter(requireContext(), tempUnit)
        binding.dailyRecycler.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.dailyRecycler.isNestedScrollingEnabled
        binding.dailyRecycler.adapter = dailyAdapter

        binding.refresh.setOnRefreshListener {
            //refreshDate()
            onViewCreated(view, savedInstanceState)
            Toast.makeText(requireContext(), "refresh", Toast.LENGTH_SHORT).show()
        }


        lifecycleScope.launch {
            cityViewModel.responseFlow.collect { result ->
                when (result) {
                    is LocationWeatherApiState.Loading -> {
                        binding.loading.visibility = View.VISIBLE
                        binding.pageLayout.visibility = View.GONE
                    }
                    is LocationWeatherApiState.Success -> {
                        binding.loading.visibility = View.GONE
                        binding.refresh.isRefreshing = false

                        val current = result.data.current

                        cityViewModel.getCurrentTime(result.data.timezone_offset)

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
                        binding.city.text = result.data.name
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
                        binding.visibiltyValue.text = (current.visibility / 1000).toString()
                        binding.pageLayout.visibility = View.VISIBLE

                    }
                    else -> {
                        binding.loading.visibility = View.VISIBLE
                        binding.pageLayout.visibility = View.GONE
                    }
                }
            }

        }

    }

    override fun onResume() {
        super.onResume()
        getCityData()
    }

    private fun getCityData() {
        if (cityPojo != null) {
            cityViewModel.getCurrentLocationResponse(
                cityPojo!!.lat,
                cityPojo!!.lon,
                Locale.getDefault().language
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTime() {
        lifecycleScope.launch {
            cityViewModel.currentTimeStateFlow.collect { dt ->
                val localDateTime = LocalDateTime.ofEpochSecond(dt, 0, java.time.ZoneOffset.UTC)
                val formattedTime = localDateTime.format(DateTimeFormatter.ofPattern("h:mm a"))
                val formattedData = localDateTime.format(DateTimeFormatter.ofPattern("EEE,dd MMMM"))
                binding.time.text = formattedTime.format(localDateTime)
                binding.date.text = formattedData.format(localDateTime)
            }
        }
    }
}