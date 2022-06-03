package com.bangkit.rawati.ui.signin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bangkit.rawati.R
import com.bangkit.rawati.data.local.datastore.AccountPreferences
import com.bangkit.rawati.data.remote.response.LoginResponse
import com.bangkit.rawati.databinding.ActivitySignInBinding
import com.bangkit.rawati.helper.ApiCallbackString
import com.bangkit.rawati.ui.main.MainActivity
import com.bangkit.rawati.ui.main.ViewModelFactory
import com.bangkit.rawati.ui.register.RegisterActivity


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var viewModel: SignInViewModel

    private var email: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = AccountPreferences.getInstance(dataStore)
        viewModel = ViewModelProvider(this, ViewModelFactory(pref))[SignInViewModel::class.java]

        binding.apply {
            txtRegister.setOnClickListener {
                startActivity(Intent(this@SignInActivity, RegisterActivity::class.java))
            }

            btnSignin.setOnClickListener {
                signin()
            }

            viewModel.getRemember()
                .observe(this@SignInActivity) {isRememberActive: Boolean ->
                    if (isRememberActive) {
                        rbRemember.isChecked = true
                        rememberMe()
                    } else {
                        rbRemember.isChecked = false
                        forgetMe()
                    }
                }
            rbRemember.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
                viewModel.saveRemember(isChecked)
            }
        }
        loadData()
        updateViews()
    }

    private fun rememberMe(){
        val sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString(EMAIL, binding.txtEmail.getText().toString())
        editor.apply()
    }

    private fun forgetMe(){
        val sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString(EMAIL, "")
        editor.apply()
    }

    private fun loadData() {
        val sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        email = sharedPreferences.getString(EMAIL, "")
    }

    private fun updateViews() {
        binding.txtEmail.setText(email)
    }

    private fun signin(){
        binding.apply {
            val loginResponse = LoginResponse(
                identifier = txtEmail.text.toString(),
                password = txtPassword.text.toString(),
                loginResult = null
            )

            load(true)

            viewModel.signin(loginResponse, object : ApiCallbackString{
                override fun onResponse(state: Boolean, message: String) {
                    process(state, message)
                }
            }) {
                if (it?.loginResult != null) {
                    it.identifier
                    it.password
                } else {
                    Toast.makeText(applicationContext, "GAGAL MASUK", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun load(state: Boolean){
        if (state){
            binding.progress.visibility = View.VISIBLE
            binding.btnSignin.visibility = View.GONE
            binding.btnSigningoogle.visibility = View.GONE
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }else {
            binding.progress.visibility = View.GONE
            binding.btnSignin.visibility = View.VISIBLE
            binding.btnSigningoogle.visibility = View.VISIBLE
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    private fun process(state: Boolean, message: String) {
        if (state) {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.info))
                setMessage(getString(R.string.login_success))
                setPositiveButton(getString(R.string.next)) { _, _ ->
                    val intent = Intent(context, MainActivity::class.java)
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