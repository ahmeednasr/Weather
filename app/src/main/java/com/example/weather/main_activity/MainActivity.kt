package com.example.weather.main_activity

import android.content.Context
import android.content.ContextWrapper
import android.graphics.drawable.ColorDrawable
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
import com.example.weather.views.home_view.ConnectivityObserver
import com.example.weather.views.home_view.NetworkConnectivityObserver
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
    private lateinit var navigationView: NavigationView
    private lateinit var mainFactory: MainFactory
    lateinit var drawerLayout: DrawerLayout
    lateinit var mainViewModel: MainViewModel
    lateinit var rootView: View
    private lateinit var connectivityObserver: ConnectivityObserver

    override fun attachBaseContext(newBase: Context) {
        val localeUpdatedContext: ContextWrapper =
            ContextUtils.updateLocale(newBase, Locale.getDefault())
        super.attachBaseContext(localeUpdatedContext)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        connectivityObserver = NetworkConnectivityObserver(applicationContext)
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
        supportFragmentManager.beginTransaction()
            .show(navHostFragment)
            .commit()
        mainFactory = MainFactory()
        mainViewModel = ViewModelProvider(this, mainFactory)[MainViewModel::class.java]

        connectivityObserver.observe().onEach { result ->
            when (result) {
                ConnectivityObserver.Status.Available -> {
                    Log.i("Connection", "Available act")
                    Snackbar.make(
                        rootView, "online mode", Snackbar.LENGTH_SHORT
                    ).show()
                }
                ConnectivityObserver.Status.Unavailable -> {
                    Log.i("Connection", "Unavailable act")
                    Snackbar.make(
                        rootView, "offline mode", Snackbar.LENGTH_SHORT
                    ).show()

                }
                ConnectivityObserver.Status.Losing -> {
                    Log.i("Connection", "Losing act")
                }
                ConnectivityObserver.Status.Lost -> {
                    Log.i("Connection", "Lost act")
                    Snackbar.make(
                        rootView, "offline mode", Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }.launchIn(lifecycleScope)
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
}
