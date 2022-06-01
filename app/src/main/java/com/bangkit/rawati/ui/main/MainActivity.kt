package com.bangkit.rawati.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
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
import com.bangkit.rawati.ui.signin.SignInActivity
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_logout -> {
                mainViewModel.signout()
                AlertDialog.Builder(this).apply {
                    setTitle(getString(R.string.info))
                    setMessage(getString(R.string.logout_success))
                    setPositiveButton(getString(R.string.next)) { _, _ ->
                        startActivity(Intent(this@MainActivity, SignInActivity::class.java))
                        finish()
                    }
                    create()
                    setCancelable(false)
                    show()
                }
            }

            R.id.menu_settings -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }

        }
        return super.onOptionsItemSelected(item)
    }
}