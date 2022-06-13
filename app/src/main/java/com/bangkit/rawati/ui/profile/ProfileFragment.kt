package com.bangkit.rawati.ui.profile

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bangkit.rawati.R
import com.bangkit.rawati.data.local.datastore.AccountPreferences
import com.bangkit.rawati.data.remote.response.ChangePassword
import com.bangkit.rawati.databinding.FragmentProfileBinding
import com.bangkit.rawati.helper.ApiCallbackString
import com.bangkit.rawati.ui.main.MainActivity
import com.bangkit.rawati.ui.main.ViewModelFactory
import com.bangkit.rawati.ui.signin.SignInActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
class ProfileFragment : Fragment() {
    private var viewModel: ProfileViewModel? = null
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        val pref = AccountPreferences.getInstance(requireContext().dataStore)
        viewModel = ViewModelProvider(this, ViewModelFactory(pref))[ProfileViewModel::class.java]

        binding.apply {
            /*llNotification.setOnClickListener {
                startActivity(Intent(requireActivity(), UpdateProfileActivity::class.java))
            }*/

            viewModel!!.getUser().observe(viewLifecycleOwner) {
                viewModel!!.getDataUser(
                    it.token,
                    it.user_id) {
                    if (it?.userResult != null) {
                        txtName.text = it?.userResult.name
                    }
                }
            }

            llSignout.setOnClickListener {
                val dialog = Dialog(requireActivity())
                val view = layoutInflater.inflate(R.layout.popup_confirm_layout, null)
                val btnyes = view.findViewById<Button>(R.id.btn_yes)
                val btnNo = view.findViewById<Button>(R.id.btn_no)
                val info = view.findViewById<TextView>(R.id.info_confirm)
                val msg = view.findViewById<TextView>(R.id.msg_confirm)

                info.text = "Logout"
                msg.text = "Are you sure want to logout?"
                btnyes.text = "YES"
                btnyes.setOnClickListener {
                    viewModel!!.signout()
                    startActivity(Intent(requireActivity(), SignInActivity::class.java))
                    requireActivity().finish()
                }

                btnNo.text = "NO"
                btnNo.setOnClickListener {
                    dialog.dismiss()
                }

                dialog.setCancelable(false)
                dialog.setContentView(view)
                dialog.show()
            }

            llPassword.setOnClickListener {
                val dialog = BottomSheetDialog(requireContext())
                val view = layoutInflater.inflate(R.layout.popup_change_password, null)
                val btnsubmit = view.findViewById<Button>(R.id.btn_submit)
                val txt_oldpass = view.findViewById<TextInputEditText>(R.id.txt_old_password)
                val txt_newpass = view.findViewById<TextInputEditText>(R.id.txt_new_password)

                btnsubmit.setOnClickListener {
                    val changePassword = ChangePassword(
                        new_password = txt_newpass.text.toString(),
                        old_password = txt_oldpass.text.toString()
                    )

                    viewModel!!.getUser().observe(requireActivity()) {
                        viewModel!!.changePassword(
                            it.token,
                            it.user_id,
                            changePassword,
                            object : ApiCallbackString{
                                override fun onResponse(state: Boolean, message: String) {
                                    processChange(state, message)
                                }
                            }) {
                            it?.new_password
                            it?.old_password
                        }
                    }
                }
                dialog.setContentView(view)
                dialog.show()
            }

            llProfile.setOnClickListener {
                startActivity(Intent(context, DetailProfileActivity::class.java))
            }

            viewModel!!.getThemeSettings().observe(viewLifecycleOwner, {
                    isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    switchTheme.isChecked = true
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    switchTheme.isChecked = false
                }
            })

            switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
                viewModel!!.saveThemeSetting(isChecked)
            }
        }

        return binding.root

    }
    @SuppressLint("InflateParams", "SetTextI18n")
    private fun processChange(state: Boolean, message: String) {
        if (state) {
            val dialog = BottomSheetDialog(requireContext())
            val view = layoutInflater.inflate(R.layout.popup_info_reset, null)
            val closebtn = view.findViewById<Button>(R.id.btn_resetsuccess)
            val imgicon = view.findViewById<ImageView>(R.id.img_icon)
            val textinfo = view.findViewById<TextView>(R.id.txt_info)
            val textmessage = view.findViewById<TextView>(R.id.txt_message)

            imgicon.setImageResource(R.drawable.ic_check)
            textinfo.text = "Change password success"
            textmessage.text = "Your password has been change, please login again"

            closebtn.setOnClickListener {
                startActivity(Intent(requireActivity(), SignInActivity::class.java))
                viewModel!!.signout()
                requireActivity().finish()
            }
            dialog.setCancelable(false)
            dialog.setContentView(view)
            dialog.show()
        } else {
            val dialog = BottomSheetDialog(requireContext())
            val view = layoutInflater.inflate(R.layout.popup_info_reset, null)
            val closebtn = view.findViewById<Button>(R.id.btn_resetsuccess)
            val imgicon = view.findViewById<ImageView>(R.id.img_icon)
            val textinfo = view.findViewById<TextView>(R.id.txt_info)
            val textmessage = view.findViewById<TextView>(R.id.txt_message)

            imgicon.setImageResource(R.drawable.ic_error)
            textinfo.text = getString(R.string.reset_failed)
            textmessage.text = "${getString(R.string.error_msg)} ${message}"

            closebtn.setOnClickListener {
                startActivity(Intent(requireActivity(), MainActivity::class.java))
                requireActivity().finish()
            }
            dialog.setCancelable(false)
            dialog.setContentView(view)
            dialog.show()
        }
    }
}