package com.bangkit.rawati.ui.profile

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bangkit.rawati.R
import com.bangkit.rawati.data.local.datastore.AccountPreferences
import com.bangkit.rawati.data.remote.response.UserProfileResponse
import com.bangkit.rawati.databinding.ActivityUpdateProfileBinding
import com.bangkit.rawati.helper.ApiCallbackString
import com.bangkit.rawati.ui.main.MainActivity
import com.bangkit.rawati.ui.main.ViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.*


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
class UpdateProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateProfileBinding
    private lateinit var viewModel: UpdateProfileViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = AccountPreferences.getInstance(dataStore)
        viewModel = ViewModelProvider(this, ViewModelFactory(pref))[UpdateProfileViewModel::class.java]

        val getCalendar = Calendar.getInstance()

        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            getCalendar.set(Calendar.YEAR, year)
            getCalendar.set(Calendar.MONTH, month)
            getCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            setCalendar(getCalendar)
        }
        binding.apply {
            btnDateBirth.setOnClickListener {
                DatePickerDialog(
                    this@UpdateProfileActivity,
                    datePicker,
                    getCalendar.get(Calendar.YEAR),
                    getCalendar.get(Calendar.MONTH),
                    getCalendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

            rgGender.setOnCheckedChangeListener { group, i ->
                var rb = findViewById<RadioButton>(i)
                if (rb != null){
                    txtGender.text = (rb.text.toString())
                }
            }

            btnSubmitProfile.setOnClickListener {
                val userProfileResponse = UserProfileResponse(
                    birth_date = dateBirth.text.toString(),
                    gender = txtGender.text.toString(),
                    height = Integer.parseInt(txtHeight.text.toString()),
                    weight = Integer.parseInt(txtWeight.text.toString()),
                    weight_goal = Integer.parseInt(txtWeightGoal.text.toString()),
                    userProfileResult = null
                )

                viewModel.getUser().observe(this@UpdateProfileActivity) {
                    viewModel.updateProfile(
                        it.token,
                        it.user_id,
                        userProfileResponse,
                        object : ApiCallbackString {
                            override fun onResponse(state: Boolean, message: String) {
                                processChange(state, message)
                            }
                        }) {
                        if (it?.userProfileResult != null) {
                            it.birth_date
                            it.gender
                            it.height
                            it.weight
                            it.weight_goal
                        }
                    }
                }
            }
        }
    }

    private fun processChange(state: Boolean, message: String) {
        if (state) {
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.popup_info_reset, null)
            val closebtn = view.findViewById<Button>(R.id.btn_resetsuccess)
            val imgicon = view.findViewById<ImageView>(R.id.img_icon)
            val textinfo = view.findViewById<TextView>(R.id.txt_info)
            val textmessage = view.findViewById<TextView>(R.id.txt_message)

            imgicon.setImageResource(R.drawable.ic_check)
            textinfo.text = "Update Profile Success"
            textmessage.text = "Your profile has been update, you can use the app now"

            closebtn.setOnClickListener {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
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
            textinfo.text = "Update profile failed"
            textmessage.text = "${getString(R.string.error_msg)} ${message}"

            closebtn.setOnClickListener {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            dialog.setCancelable(false)
            dialog.setContentView(view)
            dialog.show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setCalendar(getCalendar: Calendar) {
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        binding.btnDateBirth.text = (sdf.format(getCalendar.time))
        binding.dateBirth.text = "${ (sdf.format(getCalendar.time))}T00:00:00Z"
    }
}