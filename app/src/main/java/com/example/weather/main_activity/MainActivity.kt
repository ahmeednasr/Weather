package com.example.weather.main_activity

import android.content.Context
import android.content.ContextWrapper
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.weather.R
import com.example.weather.system.companion.ContextUtils
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
    private lateinit var navigationView: NavigationView
    private lateinit var mainFactory: MainFactory
    lateinit var drawerLayout: DrawerLayout
    lateinit var mainViewModel: MainViewModel
    lateinit var rootView: View
    override fun attachBaseContext(newBase: Context) {
        val localeUpdatedContext: ContextWrapper =
            ContextUtils.updateLocale(newBase, Locale.getDefault())
        super.attachBaseContext(localeUpdatedContext)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        rootView = findViewById(android.R.id.content)

        var actionBar = supportActionBar
        if (actionBar != null) {
            val title = actionBar.title
            val titleColor = ContextCompat.getColor(this, R.color.white)
            val styledTitle = SpannableString(title)
            styledTitle.setSpan(
                ForegroundColorSpan(titleColor),
                0,
                title?.length ?: 0,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            actionBar.title = styledTitle
            actionBar.setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        this,
                        R.color.dark_blue
                    )
                )
            )
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
            actionBar.setDisplayShowHomeEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)

            navController = Navigation.findNavController(this, R.id.navHostFragment)
            NavigationUI.setupWithNavController(navigationView, navController)
        }
        mainFactory = MainFactory()
        mainViewModel = ViewModelProvider(this, mainFactory)[MainViewModel::class.java]

        getConnection()
        lifecycleScope.launch {

            mainViewModel.connectionFlow.collect { result ->
                when (result) {
                    is ConnectionState.Success -> {
                        supportFragmentManager.beginTransaction()
                            .show(navHostFragment)
                            .commit()
                    }
                    is ConnectionState.Lose -> {
//                        supportFragmentManager.beginTransaction()
//                            .hide(navHostFragment)
//                            .commit()
                        Snackbar.make(
                            rootView, "lose Network", Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    else -> {

                    }
                }
            }

        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getConnection() {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                Log.i("NETWORK", "onAvailable")
                mainViewModel.setConnectionState(ConnectionState.Success("connect"))

            }

            override fun onLost(network: Network) {
                super.onLost(network)
                Log.i("NETWORK", "onLost")
                mainViewModel.setConnectionState(ConnectionState.Lose("lose"))

                Snackbar.make(
                    rootView, "lose Network", Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

}
