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

    fun signin(loginResponse: LoginResponse, param: ApiCallbackString, onResult: (LoginResponse?) -> Unit){
        ApiConfig.apiInstance
            .login(loginResponse)
            .enqueue(object : Callback<LoginResponse>{
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    val responseBody = response.body()
                    onResult(responseBody)
                    if (responseBody != null){
                        param.onResponse(response.body() != null, SUCCESS)
                        val user = Account(
                            responseBody.loginResult!!.token,
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

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    onResult(null)
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