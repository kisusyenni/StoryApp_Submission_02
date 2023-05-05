package com.kisusyenni.storyapp.view.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kisusyenni.storyapp.R
import com.kisusyenni.storyapp.databinding.ActivityMainBinding
import com.kisusyenni.storyapp.view.welcome.WelcomeActivity
import com.kisusyenni.storyapp.viewmodel.DatastoreViewModel
import com.kisusyenni.storyapp.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var datastoreViewModel: DatastoreViewModel

    private fun observeSession() {
        datastoreViewModel.fetchSession()
        datastoreViewModel.session.observe(this@MainActivity) { session ->
            if(!session.isLogin) Intent(this@MainActivity, WelcomeActivity::class.java).also { intent->
                Toast.makeText(this@MainActivity, resources.getString(R.string.logout_success), Toast.LENGTH_LONG).show()
                startActivity(intent)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                finish()
            }
        }
    }

    private fun observeTheme() {
        datastoreViewModel.getTheme()
        datastoreViewModel.darkMode.observe(this@MainActivity
        ) { isDarkMode: Boolean ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_maps, R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        val factory = ViewModelFactory.getInstance(this)
        datastoreViewModel = ViewModelProvider(this, factory)[DatastoreViewModel::class.java]

        observeSession()
        observeTheme()
    }

    override fun onResume() {
        super.onResume()
        observeSession()
        observeTheme()
    }

}