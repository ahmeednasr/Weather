package com.example.weather.map

import android.content.Intent
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.weather.R
import com.example.weather.system.companion.MyCompanion
import com.example.weather.main_activity.MainActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener {
    lateinit var mapView: MapView
    private lateinit var mMap: GoogleMap
    var marker: Marker? = null
    var latitude: Double? = null
    var longitude: Double? = null
    lateinit var okBtn: Button
    lateinit var geocoder: Geocoder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        supportActionBar?.hide()
        mapView = findViewById(R.id.mapView)
        okBtn = findViewById(R.id.button2)
        okBtn.setOnClickListener {
            if (latitude != null && longitude != null) {
                geocoder = Geocoder(this)
                var address =
                    geocoder.getFromLocation(latitude!!, longitude!!, 1)
                if (address != null) {
                    var newLocality = address[0].subAdminArea
                    if (newLocality != null) {
                        Toast.makeText(this, newLocality, Toast.LENGTH_SHORT).show()
                        val sharedPreferences = getSharedPreferences("PREFS", 0)
                        val editor = sharedPreferences.edit()
                        editor.putString(MyCompanion.LOCATION_KEY, MyCompanion.MAP)
                        editor.putFloat(MyCompanion.LATITUDE, latitude!!.toFloat())
                        editor.putFloat(MyCompanion.LONGITUDE, longitude!!.toFloat())
                        editor.apply()
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "not accurate Location", Toast.LENGTH_SHORT).show()
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

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Set an OnMapClickListener on the map
        mMap.setOnMapClickListener(this)

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onMapClick(latLng: LatLng) {
        mMap.clear()
        // Add a new marker at the clicked location
        latitude = latLng.latitude
        longitude = latLng.longitude
        mMap.addMarker(MarkerOptions().position(latLng).title("New Marker"))

    }
}