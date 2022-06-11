package com.bangkit.rawati.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bangkit.rawati.R
import com.bangkit.rawati.data.local.datastore.AccountPreferences
import com.bangkit.rawati.databinding.ActivityDetailProfileBinding
import com.bangkit.rawati.ui.main.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
class DetailProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailProfileBinding
    private lateinit var viewModel: DetailProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = AccountPreferences.getInstance(dataStore)
        viewModel = ViewModelProvider(this, ViewModelFactory(pref))[DetailProfileViewModel::class.java]

        binding.apply {
            btnBack.setOnClickListener {
                onBackPressed()
            }

            btnUpdate.setOnClickListener {
                startActivity(Intent(this@DetailProfileActivity, UpdateProfileActivity::class.java))
            }
        }

        viewModel.getUser().observe(this) {
            viewModel.setDataUser(
                it.token,
                it.user_id) {
                if (it?.userResult != null) {
                    Toast.makeText(applicationContext, "DATA MASUK", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, "DATA GAGAL MASUK", Toast.LENGTH_SHORT).show()
                }
            }
            viewModel.setDataUser2(
                it.token,
                it.user_id) {
                if (it?.userProfileResult != null) {
                    Toast.makeText(applicationContext, "DATA MASUK", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, "DATA GAGAL MASUK", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.getDataUser().observe(this, {
            if (it != null) {
                binding.apply {
                    txtUsername.text = it.userResult!!.username
                    txtName.text = it.userResult.name
                    txtEmail.text = it.userResult.email
                }
            }
        })

        viewModel.getDataUser2().observe(this, {
            if (it != null) {
                binding.apply {
                    if (it.userProfileResult!!.gender == "L"){
                        txtGender.text = getString(R.string.male)
                    } else if (it.userProfileResult.gender == "P") {
                        txtGender.text = getString(R.string.woman)
                    }
                    txtDateBirth.text = it.userProfileResult.birth_date
                }
            }
        })
    }
}