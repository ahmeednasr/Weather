package com.example.weather.main_activty

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.weather.R
import com.example.weather.home.location_weather_repo.LocationWeatherApiState
import com.example.weather.home.location_weather_view.LocationWeatherFactory
import com.example.weather.home.location_weather_view.LocationWeatherViewModel
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    lateinit var navControl: NavController
    private lateinit var mainFactory: MainFactory
    lateinit var mainViewModel: MainViewModel

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainFactory = MainFactory()
        mainViewModel = ViewModelProvider(this, mainFactory)[MainViewModel::class.java]
        getConnection()
        val more: AppCompatImageButton = findViewById(R.id.more_btn)
        more.setOnClickListener {
            showMenu(it)
        }
        lifecycleScope.launch {

            mainViewModel.connectionFlow.collect { result ->
                when (result) {
                    is ConnectionState.Success -> {
                        Toast.makeText(this@MainActivity, "${result.data}", Toast.LENGTH_SHORT).show()
                        val navHostFragment: NavHostFragment =
                            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
                        navControl = navHostFragment.navController
                    }
                    else -> {
                    }
                }
            }

        }

    }

    override fun onBackPressed() {
        if (navControl.currentDestination?.id != R.id.homeFragment) {
            navControl.popBackStack(R.id.homeFragment, false)
        } else {
            super.onBackPressed()
        }
    }

    private fun showMenu(view: View) {
        Log.d("TAG", "setOnClickListener called")
        val popUp = PopupMenu(this, view)
        popUp.menuInflater.inflate(R.menu.menu, popUp.menu)
        popUp.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.weatherAlertsFragment -> {
                    navControl.navigate(R.id.weatherAlertsFragment)
                    true
                }
                R.id.favoriteFragment -> {
                    navControl.navigate(R.id.favoriteFragment)
                    true
                }
                R.id.settingsFragment -> {
                    navControl.navigate(R.id.settingsFragment)
                    true
                }
                else -> false
            }
        }
        popUp.show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getConnection() {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        val connectivityManager =
            getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        // network is available for use
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            mainViewModel.setConnectionState(ConnectionState.Success("connect"))
        }

        // lost network connection
        override fun onLost(network: Network) {
            super.onLost(network)
            mainViewModel.setConnectionState(ConnectionState.Lose("lose"))

        }
    }
}