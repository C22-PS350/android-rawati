package com.bangkit.rawati.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.rawati.data.local.datastore.Account
import com.bangkit.rawati.data.local.datastore.AccountPreferences
import kotlinx.coroutines.launch

class MainViewModel(private val pref: AccountPreferences): ViewModel() {
    fun getUser(): LiveData<Account> {
        return pref.getUser().asLiveData()
    }

    fun signout() {
        viewModelScope.launch {
            pref.signout()
        }
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }
}