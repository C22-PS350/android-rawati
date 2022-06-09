package com.bangkit.rawati.ui.signin

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bangkit.rawati.R
import com.bangkit.rawati.data.local.datastore.AccountPreferences
import com.bangkit.rawati.data.remote.response.LoginResponse
import com.bangkit.rawati.data.remote.response.ResetPassword
import com.bangkit.rawati.databinding.ActivitySignInBinding
import com.bangkit.rawati.helper.ApiCallbackString
import com.bangkit.rawati.ui.main.MainActivity
import com.bangkit.rawati.ui.main.ViewModelFactory
import com.bangkit.rawati.ui.register.RegisterActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText


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

            txtForgot.setOnClickListener {
                popupForgotPassword()
            }

            viewModel.getRemember()
                .observe(this@SignInActivity) { isRememberActive: Boolean ->
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

    @SuppressLint("InflateParams")
    private fun popupForgotPassword() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.popup_forgot_password, null)
        val submit = view.findViewById<Button>(R.id.btn_resetpass)
        val txtemail = view.findViewById<TextInputEditText>(R.id.txt_email_reset)
        val loadreset = view.findViewById<ProgressBar>(R.id.progress_reset)

        submit.setOnClickListener {
            val resetPassword = ResetPassword(
                email = txtemail.text.toString()
            )
            viewModel.resetPassword(resetPassword, object : ApiCallbackString {
                override fun onResponse(state: Boolean, message: String) {
                    processReset(state, message)
                }
            }) {
                it?.email
            }
            loadreset.visibility = View.VISIBLE
            /*processReset(true, "Success")
            dialog.dismiss()*/
        }

        dialog.setContentView(view)
        dialog.show()
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    private fun processReset(state: Boolean, message: String) {
        if (state) {
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.popup_info_reset, null)
            val closebtn = view.findViewById<Button>(R.id.btn_resetsuccess)

            closebtn.setOnClickListener {
                dialog.dismiss()
            }
            dialog.setCancelable(false)
            dialog.setContentView(view)
            dialog.show()
        } else {
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.popup_info_reset, null)
            val closebtn = view.findViewById<Button>(R.id.btn_resetsuccess)
            val imgicon = view.findViewById<ImageView>(R.id.img_icon)
            val textinfo = view.findViewById<TextView>(R.id.txt_info)
            val textmessage = view.findViewById<TextView>(R.id.txt_message)

            imgicon.setImageResource(R.drawable.ic_error)
            textinfo.text = getString(R.string.reset_failed)
            textmessage.text = "${getString(R.string.error_msg)} ${message}"

            closebtn.setOnClickListener {
                startActivity(Intent(this, SignInActivity::class.java))
                finish()
            }
            dialog.setCancelable(false)
            dialog.setContentView(view)
            dialog.show()
        }
    }

    private fun rememberMe() {
        val sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString(EMAIL, binding.txtEmail.getText().toString())
        editor.apply()
    }

    private fun forgetMe() {
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

    private fun signin() {
        binding.apply {
            val loginResponse = LoginResponse(
                identifier = txtEmail.text.toString(),
                password = txtPassword.text.toString(),
                loginResult = null
            )

            load(true)

            viewModel.signin(loginResponse, object : ApiCallbackString {
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

    private fun load(state: Boolean) {
        if (state) {
            binding.progress.visibility = View.VISIBLE
            binding.btnSignin.visibility = View.GONE
            binding.btnSigningoogle.visibility = View.GONE
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        } else {
            binding.progress.visibility = View.GONE
            binding.btnSignin.visibility = View.VISIBLE
            binding.btnSigningoogle.visibility = View.VISIBLE
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    private fun process(state: Boolean, message: String) {
        if (state) {
            Toast.makeText(applicationContext, "Welcome ${binding.txtEmail.text.toString()}", Toast.LENGTH_SHORT).show()

            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
            load(false)
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