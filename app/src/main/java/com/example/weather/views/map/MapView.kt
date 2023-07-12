package com.example.weather.views.map


import android.location.Geocoder
import android.os.Bundle
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
import com.example.weather.system.companion.MyCompanion
import com.example.weather.data_source.localSource.ConcretLocalSource
import com.example.weather.data_source.search_repo.SearchApiState
import com.example.weather.data_source.search_repo.SearchRepo
import com.example.weather.data_source.search_repo.search_remote.SearchApiClient
import com.example.weather.data_source.search_repo.search_result_pojo.CityPojo
import com.example.weather.data_source.search_repo.search_result_pojo.SearchResponse
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


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
            SearchRepo.getInstance(
                SearchApiClient.getInstance(),
                ConcretLocalSource.getInstance(requireContext())
            )
        )

        mapViewModel = ViewModelProvider(this, mapFactory)[MapViewModel::class.java]

        searchBar.setOnQueryTextListener(myQuery)

        lifecycleScope.launch {
            mapViewModel.responseSearchFlow.collect { result ->
                when (result) {
                    is SearchApiState.Success -> {
                        cityRecyclerView.visibility = View.VISIBLE
                        Log.i("MySearch", " myTest-> ${result.data.toString()}")
                        mySearchResponse = result.data
                        adapter.data = mySearchResponse
                    }
                    is SearchApiState.Failure -> {
                        cityRecyclerView.visibility = View.INVISIBLE
                        Log.i("MySearch", result.msg.toString())
                        //Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
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
                MyCompanion.ALERTS_FRAGMENT -> {
                    parentFragmentManager.setFragmentResult(MyCompanion.MAP, Bundle().apply {
                        putDouble("latitude", latitude!!)
                        putDouble("longitude", longitude!!)
                    })

                    controller.navigateUp()
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
        mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
        // Set an OnMapClickListener on the map
        mMap.setOnMapClickListener(this)

        // Add a marker in Sydney and move the camera
        latitude = 21.38
        longitude = 39.85
        val mecca = LatLng(latitude!!, longitude!!)
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
        cityRecyclerView.visibility = View.INVISIBLE
        mCity = city
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