package com.bangkit.rawati.ui.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.bangkit.rawati.databinding.ActivitySplashScreenBinding
import com.bangkit.rawati.ui.signin.SignInActivity

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val time = 500

        Handler(Looper.getMainLooper()).postDelayed({
            statusLogin()
            finish()
        }, time.toLong())
    }

    private fun statusLogin(){
        startActivity(Intent(this, SignInActivity::class.java))
    }
}