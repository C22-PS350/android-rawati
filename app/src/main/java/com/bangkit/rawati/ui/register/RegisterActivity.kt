package com.bangkit.rawati.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.rawati.databinding.ActivityRegisterBinding
import com.bangkit.rawati.ui.signin.SignInActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            txtSignin.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, SignInActivity::class.java))
            }
        }
    }
}