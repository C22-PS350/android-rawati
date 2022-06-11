package com.bangkit.rawati.ui.main

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.bangkit.rawati.R
import com.bangkit.rawati.data.local.datastore.AccountPreferences
import com.bangkit.rawati.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ismaeldivita.chipnavigation.ChipNavigationBar

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = AccountPreferences.getInstance(dataStore)
        mainViewModel = ViewModelProvider(this, ViewModelFactory(pref))[MainViewModel::class.java]

        bottomNav()
    }

    private fun bottomNav() {
        val navController: NavController = Navigation.findNavController(this, R.id.fragment)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navbar)
        val chipNavigationBar: ChipNavigationBar = findViewById(R.id.bottomNav)
        chipNavigationBar.setOnItemSelectedListener { itemId ->
            bottomNavigationView.selectedItemId = itemId
        }
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.dashboardFragment, R.id.activityFragment, R.id.recommendationFragment, R.id.profileFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)

        NavigationUI.setupWithNavController(bottomNavigationView, navController)
    }
}