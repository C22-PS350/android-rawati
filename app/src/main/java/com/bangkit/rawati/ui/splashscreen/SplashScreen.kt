package com.bangkit.rawati.ui.splashscreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bangkit.rawati.data.local.datastore.AccountPreferences
import com.bangkit.rawati.databinding.ActivitySplashScreenBinding
import com.bangkit.rawati.ui.main.MainActivity
import com.bangkit.rawati.ui.main.MainViewModel
import com.bangkit.rawati.ui.main.ViewModelFactory
import com.bangkit.rawati.ui.signin.SignInActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val time = 500

        Handler(Looper.getMainLooper()).postDelayed({
            statusLogin()
            finish()
        }, time.toLong())

        val pref = AccountPreferences.getInstance(dataStore)
        viewModel = ViewModelProvider(this, ViewModelFactory(pref))[MainViewModel::class.java]

        viewModel.getThemeSettings().observe(this,{
                isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        })
    }

    private fun statusLogin(){
        viewModel.getUser().observe(this) {
            if (it.isLogin) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, SignInActivity::class.java))
                finish()
            }
        }
    }
}