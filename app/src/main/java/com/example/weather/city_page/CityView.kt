package com.example.weather.city_page

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.companion.MyCompanion
import com.example.weather.databinding.FragmentCityViewBinding
import com.example.weather.home.location_weather_repo.LocationWeatherApiState
import com.example.weather.home.location_weather_repo.LocationWeatherRepo
import com.example.weather.home.location_weather_repo.remote.LocationWeatherApiClient
import com.example.weather.home.location_weather_view.*
import com.example.weather.localSource.ConcretLocalSource
import com.example.weather.map.repo.search_result_pojo.CityPojo
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class CityView : Fragment() {
    private lateinit var cityFactory: LocationWeatherFactory
    lateinit var cityViewModel: LocationWeatherViewModel
    lateinit var binding: FragmentCityViewBinding
    lateinit var geocoder: Geocoder
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

        cityViewModel.getTime()
        binding.refresh.setOnRefreshListener {
            //refreshDate()
            onViewCreated(view, savedInstanceState)
            //homeViewModel.getCurrentLocationResponse(lat, long, Locale.getDefault().language)
            //  requireActivity().recreate()

            //getLastDetails()
            Toast.makeText(requireContext(), "refresh", Toast.LENGTH_SHORT).show()
        }
        getCityData()

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
                        getTime()
                        val current = result.data.current

                        geocoder = Geocoder(requireContext(), Locale.getDefault())
                        val addressList =
                            geocoder.getFromLocation(result.data.lat, result.data.lon, 1)
                        Log.i("err", "address name: $addressList")

                        if (addressList != null && addressList.isNotEmpty()) {
                            var address = addressList[0]
                            var newLocality = addressList[0].locality
                            var subAdmin = addressList[0].subAdminArea
                            var subLocality = addressList[0].subLocality

                            var cityName = newLocality ?: subLocality ?: subAdmin
                            //  val cityName = address.subAdminArea
                            Log.i("err", "City in name: $cityName")
                            if (cityName != null && cityName.isNotEmpty()) {
                                binding.city.text = cityName
                                Log.i("err", "City in name: $cityName")
                            }
                        }

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

    private fun getCityData() {
        if (cityPojo != null) {
            cityViewModel.getCurrentLocationResponse(
                cityPojo!!.lat,
                cityPojo!!.lon,
                Locale.getDefault().language
            )
        }
    }

    private fun getTime() {
        lifecycleScope.launch {
            cityViewModel.currentTimeStateFlow.collect { time ->
                val dateFormat = SimpleDateFormat("EEE,dd MMMM", Locale.getDefault())
                val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
                binding.date.text = dateFormat.format(time)
                binding.time.text = timeFormat.format(time)

            }
        }
    }
}