package com.example.weather.map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.weather.R
import com.example.weather.companion.MyCompanion
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MyMapFragment : Fragment(), OnMapReadyCallback {
    lateinit var mapView: MapView
    var marker: Marker? = null
    var latitude: Double? = null
    var longitude: Double? = null
    lateinit var okBtn: Button
    lateinit var geocoder: Geocoder

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

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        okBtn.setOnClickListener {
            if (latitude != null && longitude != null) {
                geocoder = Geocoder(requireContext())
                var address =
                    geocoder.getFromLocation(latitude!!, longitude!!, 1)
                if (address != null) {
                    var newLocality = address[0].subAdminArea
                    if (newLocality!=null){
                        Toast.makeText(requireContext(), newLocality, Toast.LENGTH_SHORT).show()
                        val sharedPreferences = requireContext().getSharedPreferences("PREFS", 0)
                        val editor = sharedPreferences.edit()
                        editor.putFloat(MyCompanion.LATITUDE, latitude!!.toFloat())
                        editor.putFloat(MyCompanion.LONGITUDE, longitude!!.toFloat())
                        editor.apply()
                        Log.i("city", "new $newLocality")
                    }else{
                        Toast.makeText(requireContext(), "not accurate Location", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
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
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        googleMap.isMyLocationEnabled = true
        // Add a marker at Sydney and move the camera to that location
        // googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        googleMap.setOnMapClickListener { latLng ->
            marker?.remove()
            marker = googleMap.addMarker(MarkerOptions().position(latLng))
            latitude = latLng.latitude
            longitude = latLng.longitude
        }

    }
}