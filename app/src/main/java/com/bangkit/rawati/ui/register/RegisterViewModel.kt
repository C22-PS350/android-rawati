package com.bangkit.rawati.ui.register

import android.util.Log
import androidx.lifecycle.ViewModel
import com.bangkit.rawati.data.local.datastore.Account
import com.bangkit.rawati.data.local.datastore.AccountPreferences
import com.bangkit.rawati.data.remote.api.ApiConfig
import com.bangkit.rawati.data.remote.response.RegisterResponse
import com.bangkit.rawati.helper.ApiCallbackString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel (private val pref: AccountPreferences): ViewModel() {

    fun register(registerResponse: RegisterResponse, param: ApiCallbackString, onResult: (RegisterResponse?) -> Unit){
        ApiConfig.apiInstance
            .register(registerResponse)
            .enqueue(object : Callback<RegisterResponse>{
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    val responseBody = response.body()
                    onResult(responseBody)
                    if (responseBody != null){
                        param.onResponse(response.body() != null, SUCCESS)
                        val user = Account(
                            responseBody.registerResult!!.token,
                            true
                        )
                        saveAccount(user)
                    } else {
                        Log.e(TAG, "onFailure: ${response.code()}")
                        val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
                        val message = jsonObject.getString("error")
                        param.onResponse(false, message)
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    onResult(null)
                }
            })
    }

    fun saveAccount(user: Account){
        CoroutineScope(Dispatchers.IO).launch {
            pref.saveUser(user)
        }
    }

    companion object {
        const val TAG = "RegisterViewModel"
        const val SUCCESS = "success"
    }
}