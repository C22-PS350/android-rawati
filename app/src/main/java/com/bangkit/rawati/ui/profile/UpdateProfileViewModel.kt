package com.bangkit.rawati.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bangkit.rawati.data.local.datastore.Account
import com.bangkit.rawati.data.local.datastore.AccountPreferences
import com.bangkit.rawati.data.remote.api.ApiConfig
import com.bangkit.rawati.data.remote.response.UserProfileResponse
import com.bangkit.rawati.helper.ApiCallbackString
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateProfileViewModel (private val pref: AccountPreferences): ViewModel(){
    
    fun updateProfile(token: String, user_id: String, userProfileResponse: UserProfileResponse, param: ApiCallbackString, onResult: (UserProfileResponse?) -> Unit){
        ApiConfig.apiInstance
            .updateProfile("Bearer $token", user_id, userProfileResponse)
            .enqueue(object : Callback<UserProfileResponse> {
                override fun onResponse(
                    call: Call<UserProfileResponse>,
                    response: Response<UserProfileResponse>
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

                override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                    onResult(null)
                }
            })
    }

    fun getUser(): LiveData<Account> {
        return pref.getUser().asLiveData()
    }

    companion object {
        const val TAG = "ProfileViewModel"
        const val SUCCESS = "success"
    }
}