package com.example.weather.map

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import com.example.weather.R
import com.example.weather.companion.MyCompanion
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
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
        Toast.makeText(requireContext(), "h my fragment ", Toast.LENGTH_SHORT).show()
        searchBar = view.findViewById(R.id.auto_complete_text_view)
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                var location: String = searchBar.query.toString()
                var addressList: List<Address>? = null
                if (location != null) {
                    var geocoder: Geocoder = Geocoder(requireContext())
                    try {
                        addressList = geocoder.getFromLocationName(location, 1)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    if (addressList != null) {
                        var address: Address = addressList[0]
                        var latLng: LatLng = LatLng(address.latitude, address.longitude)
                        mMap.addMarker(MarkerOptions().position(latLng).title(location))
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
                    }
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                return false
            }
        })
        okBtn.setOnClickListener {
            if (latitude != null && longitude != null) {
                geocoder = Geocoder(requireContext())
                var address =
                    geocoder.getFromLocation(latitude!!, longitude!!, 1)
                if (address != null) {
                    var newLocality = address[0].subAdminArea
                    if (newLocality != null) {
                        Toast.makeText(requireContext(), newLocality, Toast.LENGTH_SHORT).show()
                        val sharedPreferences = requireContext().getSharedPreferences("PREFS", 0)
                        val editor = sharedPreferences.edit()
                        editor.putFloat(MyCompanion.LATITUDE, latitude!!.toFloat())
                        editor.putFloat(MyCompanion.LONGITUDE, longitude!!.toFloat())
                        editor.apply()
                        Log.i("city", "new $newLocality")
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

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
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