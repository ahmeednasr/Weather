package com.example.weather.map


import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.companion.MyCompanion
import com.example.weather.localSource.ConcretLocalSource
import com.example.weather.map.repo.ApiState
import com.example.weather.map.repo.Repo
import com.example.weather.map.repo.search_remote.SearchApiClient
import com.example.weather.map.repo.search_result_pojo.CityPojo
import com.example.weather.map.repo.search_result_pojo.SearchResponse
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException


class MapView : Fragment(), OnCitySelected, OnMapReadyCallback, GoogleMap.OnMapClickListener {
    lateinit var mapView: MapView
    var latitude: Double? = null
    var longitude: Double? = null
    lateinit var okBtn: Button
    lateinit var geocoder: Geocoder
    private lateinit var mMap: GoogleMap
    private lateinit var searchBar: SearchView
    private lateinit var controller: NavController
    private lateinit var mapFactory: MapViewModelFactory
    private lateinit var mapViewModel: MapViewModel
    private lateinit var adapter: CityAdapter
    private lateinit var cityRecyclerView: RecyclerView
    private var mCity: CityPojo? = null
    lateinit var mySearchResponse: SearchResponse
    private val stateFlow = MutableStateFlow("")
    var myRequest: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_my_map, container, false)
        mapView = root.findViewById(R.id.map_view)
        okBtn = root.findViewById(R.id.ok_btn)
        searchBar = root.findViewById(R.id.auto_complete_text_view)
        cityRecyclerView = root.findViewById(R.id.city_recomended_recycl)
        // Inflate the layout for this fragment
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val flag = MapViewArgs.fromBundle(arguments!!).flag

        controller = Navigation.findNavController(view)
        geocoder = Geocoder(requireContext())
        adapter = CityAdapter(this)
        cityRecyclerView.adapter = adapter
        cityRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        mapFactory = MapViewModelFactory(
            Repo.getInstance(
                SearchApiClient.getInstance(),
                ConcretLocalSource.getInstance(requireContext())
            )
        )

        mapViewModel = ViewModelProvider(this, mapFactory)[MapViewModel::class.java]

        searchBar.setOnQueryTextListener(myQuery)

        lifecycleScope.launch {
            mapViewModel.responseSearchFlow.collect { result ->
                when (result) {
                    is ApiState.Success -> {
                        cityRecyclerView.visibility = View.VISIBLE
                        Log.i("MySearch", " myTest-> ${result.data.toString()}")
                        mySearchResponse = result.data
                        adapter.data=mySearchResponse
                        //adapter.submitList(mySearchResponse)
//                        stateFlow.value = myRequest
//                        stateFlow.map { st ->
//                            mySearchResponse.filter { item ->
//                                item.name.startsWith(
//                                    st,
//                                    true
//                                )
//                            }
//                        }.collect {
//
//                        }adapter.submitList(it)
                    }
                    is ApiState.Failure -> {
                        cityRecyclerView.visibility = View.INVISIBLE
                        Log.i("MySearch", result.msg.toString())
                        Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        //loading
                        cityRecyclerView.visibility = View.INVISIBLE
                    }
                }


            }
        }
        okBtn.setOnClickListener {
            when (flag) {
                MyCompanion.SETTINGS_FRAGMENT -> {
                    val sharedPreferences =
                        requireContext().getSharedPreferences("PREFS", 0)
                    val editor = sharedPreferences.edit()
                    editor.putString(MyCompanion.LOCATION_KEY, MyCompanion.MAP)
                    editor.putFloat(MyCompanion.LATITUDE, latitude!!.toFloat())
                    editor.putFloat(MyCompanion.LONGITUDE, longitude!!.toFloat())
                    editor.apply()
                    Toast.makeText(
                        requireContext(),
                        "saved",
                        Toast.LENGTH_SHORT
                    ).show()
                    controller.popBackStack()
                }

                MyCompanion.FAV_FRAGMENT -> {
                    if (mCity != null) {
                        Log.i("city", "new $mCity")
                        mapViewModel.saveCity(mCity!!)
                        Toast.makeText(
                            requireContext(),
                            "saved",
                            Toast.LENGTH_SHORT
                        ).show()
                        controller.popBackStack()
                    }

                }
            }
        }

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Set an OnMapClickListener on the map
        mMap.setOnMapClickListener(this)

        // Add a marker in Sydney and move the camera
        val mecca = LatLng(21.38, 39.85)
        mMap.addMarker(MarkerOptions().position(mecca).title("Mecca"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mecca))
    }

    override fun onMapClick(latLng: LatLng) {
        mMap.clear()
        latitude = latLng.latitude
        longitude = latLng.longitude
        val addressList =
            geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

        Log.i("err", "address name: $addressList")
        if (addressList != null && addressList.isNotEmpty()) {

            val newLocality = addressList[0].locality
            val subAdmin = addressList[0].subAdminArea
            val country = addressList[0].countryCode
            val subLocality = addressList[0].subLocality

            val cityName = newLocality ?: subAdmin ?: subLocality
            if (cityName != null) {
                mCity = CityPojo(country, latLng.latitude, latLng.longitude, cityName, cityName)
            } else {
                Toast.makeText(
                    requireContext(),
                    "name not found",
                    Toast.LENGTH_SHORT
                ).show()
            }

            Log.i("err", "City in name: $cityName")
        }
        mMap.addMarker(MarkerOptions().position(latLng).title("New Marker"))
    }

    override fun selectCity(city: CityPojo) {
        mCity = city
//        val address =
//            geocoder.getFromLocation(city.lat, city.lon, 1)
        mMap.clear()
        latitude = city.lat
        longitude = city.lon
        val latLng = LatLng(city.lat, city.lon)
        mMap.addMarker(MarkerOptions().position(latLng).title(city.name))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))

    }

    private val myQuery = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(p0: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(p0: String?): Boolean {
            mapViewModel.search(p0.toString())
            // stateFlow.value = p0.toString()
            return false
        }
    }
}