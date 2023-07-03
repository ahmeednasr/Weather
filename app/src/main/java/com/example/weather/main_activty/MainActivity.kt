package com.example.weather.main_activty

import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.weather.R
import com.example.weather.companion.ContextUtils
import com.example.weather.companion.MyCompanion
import com.google.android.material.navigation.NavigationView
import org.intellij.lang.annotations.Language
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
    private lateinit var navigationView: NavigationView
    private lateinit var mainFactory: MainFactory
    lateinit var drawerLayout: DrawerLayout
    lateinit var mainViewModel: MainViewModel
    override fun attachBaseContext(newBase: Context) {
        val localeUpdatedContext: ContextWrapper =
            ContextUtils.updateLocale(newBase, Locale.getDefault())
        super.attachBaseContext(localeUpdatedContext)
    }

    // @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
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
//        mainFactory = MainFactory()
//        mainViewModel = ViewModelProvider(this, mainFactory)[MainViewModel::class.java]
//        getConnection()
//
//        lifecycleScope.launch {
//
//            mainViewModel.connectionFlow.collect { result ->
//                when (result) {
//                    is ConnectionState.Success -> {
//                            Toast.makeText(this@MainActivity, "${result.data}", Toast.LENGTH_SHORT)
//                                .show()
////                            val navHostFragment: NavHostFragment =
////                                supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
////                            navControl = navHostFragment.navController
//
//                    }
//                    else -> {
//                    }
//                }
//            }
//
//        }

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

//    @RequiresApi(Build.VERSION_CODES.M)
//    fun getConnection() {
//        val networkRequest = NetworkRequest.Builder()
//            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
//            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
//            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
//            .build()
//
//        val connectivityManager =
//            getSystemService(ConnectivityManager::class.java) as ConnectivityManager
//        connectivityManager.requestNetwork(networkRequest, networkCallback)
//    }
//
//    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
//        // network is available for use
//        override fun onAvailable(network: Network) {
//            super.onAvailable(network)
//            mainViewModel.setConnectionState(ConnectionState.Success("connect"))
//        }
//
//        // lost network connection
//        override fun onLost(network: Network) {
//            super.onLost(network)
//            mainViewModel.setConnectionState(ConnectionState.Lose("lose"))
//
//        }
//    }
}
