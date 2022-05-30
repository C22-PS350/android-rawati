package com.bangkit.rawati.ui.signin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.rawati.databinding.ActivitySignInBinding
import com.bangkit.rawati.ui.main.MainActivity
import com.bangkit.rawati.ui.register.RegisterActivity

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnSignup.setOnClickListener {
                startActivity(Intent(this@SignInActivity, RegisterActivity::class.java))
            }

            btnSignin.setOnClickListener {
                startActivity(Intent(this@SignInActivity, MainActivity::class.java))
                finish()
            }
        }
    }
}