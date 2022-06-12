package com.bangkit.rawati.ui.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bangkit.rawati.data.local.datastore.Account
import com.bangkit.rawati.data.local.datastore.AccountPreferences
import com.bangkit.rawati.data.remote.api.ApiConfig
import com.bangkit.rawati.data.remote.response.AllExerciseActivityRequest
import com.bangkit.rawati.data.remote.response.AllFoodActivityRequest
import com.bangkit.rawati.data.remote.response.UserProfileResponse
import com.bangkit.rawati.data.remote.response.UserResponse
import com.bangkit.rawati.ui.activity.ActivityViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class DashboardViewModel(private val pref: AccountPreferences) : ViewModel() {
    val user = MutableLiveData<UserResponse>()
    val foods = MutableLiveData<AllFoodActivityRequest>()
    val exercises = MutableLiveData<AllExerciseActivityRequest>()
    val profile = MutableLiveData<UserProfileResponse>()

    /**
     * Get today's date
     */
    fun getDate(): Date {
        val todayCal = Calendar.getInstance()
        return todayCal.time
    }

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

    fun getProfilUser(
        token: String,
        user_id: String,
        onResult: (UserProfileResponse?) -> Unit
    ) {
        ApiConfig.apiInstance
            .getProfile("Bearer $token", user_id)
            .enqueue(object : Callback<UserProfileResponse> {
                override fun onResponse(
                    call: Call<UserProfileResponse>,
                    response: Response<UserProfileResponse>
                ) {
                    val responseBody = response.body()
                    onResult(responseBody)
                    if (responseBody != null) {
                        profile.postValue(response.body())
                    } else {
                        Log.e(TAG, "onFailure: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }
            })
    }

    // Activity (Exercise) Endpoint
    fun getExerciseActivity(
        token: String, user_id: String, date: String,
        onResult: (AllExerciseActivityRequest?) -> Unit) {
        ApiConfig.apiInstance
            .getExerciseActivity("Bearer $token", user_id, date)
            .enqueue(object : Callback<AllExerciseActivityRequest>{
                override fun onResponse(
                    call: Call<AllExerciseActivityRequest>,
                    response: Response<AllExerciseActivityRequest>
                ) {
                    val responseBody = response.body()
                    onResult(responseBody)
                    if (responseBody != null) {
                        exercises.postValue(response.body())
                    } else {
                        Log.e(ActivityViewModel.TAG, "onFailure: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<AllExerciseActivityRequest>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }
            })
    }
    // Activity (Food) Endpoint
    fun getFoodActivity(
        token: String, user_id: String, date: String,
        onResult: (AllFoodActivityRequest?) -> Unit) {
        ApiConfig.apiInstance
            .getFoodActivity("Bearer $token", user_id, date)
            .enqueue(object : Callback<AllFoodActivityRequest> {
                override fun onResponse(
                    call: Call<AllFoodActivityRequest>,
                    response: Response<AllFoodActivityRequest>
                ) {
                    val responseBody = response.body()
                    onResult(responseBody)
                    if (responseBody != null) {
                        foods.postValue(response.body())
                    } else {
                        Log.e(ActivityViewModel.TAG, "onFailure: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<AllFoodActivityRequest>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }
            })
    }

    fun getUser(): LiveData<Account> {
        return pref.getUser().asLiveData()
    }

    companion object {
        const val TAG = "DashboardViewModel"
    }
}