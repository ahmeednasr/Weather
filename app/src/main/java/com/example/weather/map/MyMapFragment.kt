package com.example.weather.map


import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.weather.R
import com.example.weather.companion.MyCompanion
import com.example.weather.favorite.favorite_view.FavoriteViewDirections
import com.example.weather.search.search_repo.search_result_pojo.CityPojo
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

import java.io.IOException

class MyMapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMapClickListener {
    lateinit var mapView: MapView
    var marker: Marker? = null
    var latitude: Double? = null
    var longitude: Double? = null
    lateinit var okBtn: Button
    lateinit var geocoder: Geocoder
    private lateinit var mMap: GoogleMap
    private lateinit var searchBar: SearchView
    lateinit var controller: NavController

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
        // Inflate the layout for this fragment
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controller = Navigation.findNavController(view)
        geocoder = Geocoder(requireContext())

        val flag = MyMapFragmentArgs.fromBundle(arguments!!).flag
        Log.i("FLAG", flag)
        searchBar = view.findViewById(R.id.auto_complete_text_view)
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                val location: String = searchBar.query.toString()
                var addressList: List<Address>? = null
                try {
                    addressList = geocoder.getFromLocationName(location, 3)

                    Log.i("FLAG", addressList.toString())
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                if (addressList != null && addressList.isNotEmpty()) {
                    mMap.clear()
                    Log.i("MyList", addressList.toString())
                    val address: Address = addressList[0]
                    latitude = address.latitude
                    longitude = address.longitude

                    val latLng: LatLng = LatLng(address.latitude, address.longitude)
                    mMap.addMarker(MarkerOptions().position(latLng).title(location))
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
                }

                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })
        okBtn.setOnClickListener {
            if (latitude != null && longitude != null) {
                val address =
                    geocoder.getFromLocation(latitude!!, longitude!!, 3)
                if (address != null) {
                    Log.i("FLAG", address.toString())
                    val cityDetails = address[0]

                    val newLocality = address[0].locality
                    val subAdmin = address[0].subAdminArea
                    val subLocality = address[0].subLocality

                    val cityName = newLocality ?: subLocality ?: subAdmin
                    Log.i("FLAG", " test me $newLocality ,$subLocality ,$subAdmin")
                    if (newLocality != null) {
                        Toast.makeText(requireContext(), cityName, Toast.LENGTH_SHORT).show()
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
                                    "saved$latitude",
                                    Toast.LENGTH_SHORT
                                ).show()
                                controller.navigate(MyMapFragmentDirections.actionMyMapFragmentToSettingsFragment())
                            }
                            MyCompanion.FAV_FRAGMENT -> {

                                val city: CityPojo = CityPojo(
                                    cityDetails.countryCode,
                                    cityDetails.latitude,
                                    cityDetails.longitude,
                                    cityDetails.locality,
                                    cityDetails.subAdminArea
                                )
                                Log.i("city", "new $city")
                                val action =
                                    MyMapFragmentDirections.actionMyMapFragmentToFavoriteFragment()
                                action.city = city
                                action.flag = true
                                controller.navigate(action)
                            }
                        }
                        Log.i("city", "new $cityName")
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "not accurate Location",
                            Toast.LENGTH_SHORT
                        ).show()
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
        val Mecca = LatLng(21.38, 39.85)
        mMap.addMarker(MarkerOptions().position(Mecca).title("Marker in Mecca"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Mecca))
    }

    override fun onMapClick(latLng: LatLng) {
        mMap.clear()
        latitude = latLng.latitude
        longitude = latLng.longitude
        mMap.addMarker(MarkerOptions().position(latLng).title("New Marker"))
    }
}