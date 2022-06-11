package com.bangkit.rawati.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.rawati.data.local.datastore.AccountPreferences
import com.bangkit.rawati.ui.activity.ActivityViewModel
import com.bangkit.rawati.ui.dashboard.DashboardViewModel
import com.bangkit.rawati.ui.profile.DetailProfileViewModel
import com.bangkit.rawati.ui.profile.ProfileViewModel
import com.bangkit.rawati.ui.profile.UpdateProfileViewModel
import com.bangkit.rawati.ui.register.RegisterViewModel
import com.bangkit.rawati.ui.signin.SignInViewModel

class ViewModelFactory (private val pref: AccountPreferences) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(pref) as T
            }
            modelClass.isAssignableFrom(SignInViewModel::class.java) -> {
                SignInViewModel(pref) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(pref) as T
            }
            modelClass.isAssignableFrom(DashboardViewModel::class.java) -> {
                DashboardViewModel(pref) as T
            }
            modelClass.isAssignableFrom(DetailProfileViewModel::class.java) -> {
                DetailProfileViewModel(pref) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(pref) as T
            }
            modelClass.isAssignableFrom(ActivityViewModel::class.java) -> {
                ActivityViewModel(pref) as T
            }
            modelClass.isAssignableFrom(UpdateProfileViewModel::class.java) -> {
                UpdateProfileViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}