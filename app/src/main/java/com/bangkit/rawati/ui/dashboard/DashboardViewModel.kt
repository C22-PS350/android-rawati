package com.bangkit.rawati.ui.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bangkit.rawati.data.local.datastore.Account
import com.bangkit.rawati.data.local.datastore.AccountPreferences
import com.bangkit.rawati.data.remote.api.ApiConfig
import com.bangkit.rawati.data.remote.response.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardViewModel(private val pref: AccountPreferences) : ViewModel() {
    val user = MutableLiveData<UserResponse>()

    fun setDataUser(
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
                        Log.e(TAG, "onFailure: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }
            })
    }

    fun getDataUser(): LiveData<UserResponse> {
        return user
    }

    fun getUser(): LiveData<Account> {
        return pref.getUser().asLiveData()
    }

    companion object {
        const val TAG = "DashboardViewModel"
    }
}