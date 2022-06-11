package com.bangkit.rawati.ui.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bangkit.rawati.R
import com.bangkit.rawati.data.local.datastore.AccountPreferences
import com.bangkit.rawati.data.remote.response.RegisterResponse
import com.bangkit.rawati.databinding.ActivityRegisterBinding
import com.bangkit.rawati.helper.ApiCallbackString
import com.bangkit.rawati.ui.main.ViewModelFactory
import com.bangkit.rawati.ui.profile.UpdateProfileActivity
import com.bangkit.rawati.ui.signin.SignInActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = AccountPreferences.getInstance(dataStore)
        viewModel = ViewModelProvider(this, ViewModelFactory(pref))[RegisterViewModel::class.java]

        binding.apply {

            btnRegister.setOnClickListener {
                register()
            }

            txtSignin.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, SignInActivity::class.java))
            }

        }
    }

    private fun register(){
        binding.apply {
            val registerResponse = RegisterResponse(
                email = txtEmail.text.toString(),
                name = txtName.text.toString(),
                password = txtPassword.text.toString(),
                username = txtUsername.text.toString(),
                registerResult = null
            )

            load(true)

            viewModel.register(registerResponse, object : ApiCallbackString{
                override fun onResponse(state: Boolean, message: String) {
                    process(state, message)
                }
            }) {
                if (it?.registerResult != null) {
                    it.email
                    it.name
                    it.password
                    it.username
                } else {
                    Toast.makeText(applicationContext, "GAGAL MASUK", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun load(state: Boolean){
        if (state){
            binding.progress.visibility = View.VISIBLE
            binding.btnRegister.visibility = View.GONE
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        } else {
            binding.progress.visibility = View.GONE
            binding.btnRegister.visibility = View.VISIBLE
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    private fun process(state: Boolean, message: String) {
        if (state) {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.info))
                setMessage(getString(R.string.login_success))
                setPositiveButton(getString(R.string.next)) { _, _ ->
                    val intent = Intent(context, UpdateProfileActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                load(false)
                setCancelable(false)
                create()
                show()
            }
        } else {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.info))
                setMessage("${getString(R.string.login_failed)}, $message")
                setPositiveButton(getString(R.string.next)) { _, _ ->
                    binding.progress.visibility = View.GONE
                }
                load(false)
                setCancelable(false)
                create()
                show()
            }
        }
    }

    companion object {
        private const val EMAIL = "email"
        private const val SHARED_PREFS = "sharedPrefs"
    }
}