package com.bangkit.rawati.ui.profile

import android.util.Log
import androidx.lifecycle.*
import com.bangkit.rawati.data.local.datastore.Account
import com.bangkit.rawati.data.local.datastore.AccountPreferences
import com.bangkit.rawati.data.remote.api.ApiConfig
import com.bangkit.rawati.data.remote.response.ChangePassword
import com.bangkit.rawati.data.remote.response.UserResponse
import com.bangkit.rawati.helper.ApiCallbackString
import com.bangkit.rawati.ui.dashboard.DashboardViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel (private val pref: AccountPreferences): ViewModel(){
    val user = MutableLiveData<UserResponse>()

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

    fun getDataUser(
        token: String,
        user_id: String,
        onResult: (UserResponse?) -> Unit
    ) {
        ApiConfig.apiInstance
            .getDataUser("Bearer $token", user_id)
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    val responseBody = response.body()
                    onResult(responseBody)
                    if (responseBody != null) {
                        user.postValue(response.body())
                    } else {
                        Log.e(DashboardViewModel.TAG, "onFailure: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
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