package com.bangkit.rawati.ui.signin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.rawati.data.local.datastore.Account
import com.bangkit.rawati.data.local.datastore.AccountPreferences
import com.bangkit.rawati.data.remote.api.ApiConfig
import com.bangkit.rawati.data.remote.response.LoginResponse
import com.bangkit.rawati.helper.ApiCallbackString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInViewModel (private val pref: AccountPreferences): ViewModel() {

    fun signin(email: String, pass: String, param: ApiCallbackString){
        ApiConfig.apiInstance
            .login(email, pass)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful){
                        val responseBody = response.body()
                        if (responseBody != null && !responseBody.error){
                            param.onResponse(response.body() != null, SUCCESS)
                            val user = Account(
                                responseBody.loginResult.name,
                                email,
                                pass,
                                responseBody.loginResult.userId,
                                responseBody.loginResult.token,
                                true
                            )
                            saveAccount(user)
                        }
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                        val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
                        val message = jsonObject.getString("message")
                        param.onResponse(false, message)
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.d(TAG, "onFailure: ${t.message}")
                    param.onResponse(false, t.message.toString())
                }

            })
    }

    fun saveAccount(user: Account){
        CoroutineScope(Dispatchers.IO).launch {
            pref.saveUser(user)
        }
    }

    fun getRemember(): LiveData<Boolean> {
        return pref.getRemember().asLiveData()
    }

    fun saveRemember(isRememberActive: Boolean) {
        viewModelScope.launch {
            pref.saveRemember(isRememberActive)
        }
    }

    companion object {
        const val TAG = "SignInViewModel"
        const val SUCCESS = "success"
    }
}