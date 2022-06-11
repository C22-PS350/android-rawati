package com.bangkit.rawati.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.rawati.data.local.datastore.Account
import com.bangkit.rawati.data.local.datastore.AccountPreferences
import com.bangkit.rawati.data.remote.api.ApiConfig
import com.bangkit.rawati.data.remote.response.ChangePassword
import com.bangkit.rawati.helper.ApiCallbackString
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel (private val pref: AccountPreferences): ViewModel(){

    fun changePassword(token: String, user_id: String, changePassword: ChangePassword, param: ApiCallbackString, onResult: (ChangePassword?) -> Unit){
        ApiConfig.apiInstance
            .changePassword("Bearer $token", user_id, changePassword)
            .enqueue(object : Callback<ChangePassword> {
                override fun onResponse(
                    call: Call<ChangePassword>,
                    response: Response<ChangePassword>
                ) {
                    val responseBody = response.body()
                    onResult(responseBody)
                    if (responseBody != null){
                        param.onResponse(response.body() != null, SUCCESS)
                    } else {
                        Log.e(TAG, "onFailure: ${response.code()}")
                        val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
                        val message = jsonObject.getString("error")
                        param.onResponse(false, message)
                    }
                }

                override fun onFailure(call: Call<ChangePassword>, t: Throwable) {
                    onResult(null)
                }
            })
    }

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

    companion object {
        const val TAG = "ProfileViewModel"
        const val SUCCESS = "success"
    }
}