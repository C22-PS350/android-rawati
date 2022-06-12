package com.bangkit.rawati.ui.recommendation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bangkit.rawati.data.local.datastore.Account
import com.bangkit.rawati.data.local.datastore.AccountPreferences
import com.bangkit.rawati.data.remote.api.ApiConfig
import com.bangkit.rawati.data.remote.response.ExerciseRecommendationResponse
import com.bangkit.rawati.data.remote.response.FoodRecommendationResponse
import com.bangkit.rawati.data.remote.response.RecommendationRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecommendationViewModel (private val pref: AccountPreferences): ViewModel() {
    val foodRecommendation = MutableLiveData<FoodRecommendationResponse>()
    val exerciseRecommendation = MutableLiveData<ExerciseRecommendationResponse>()

    fun getFoodRecommendations(
        token: String, recommendationRequest: RecommendationRequest,
        onResult: (FoodRecommendationResponse?) -> Unit) {
        ApiConfig.apiInstance
            .getFoodRecommendations("Bearer $token", recommendationRequest)
            .enqueue(object : Callback<FoodRecommendationResponse> {
                override fun onResponse(
                    call: Call<FoodRecommendationResponse>,
                    response: Response<FoodRecommendationResponse>
                ) {
                    val responseBody = response.body()
                    onResult(responseBody)
                    if (responseBody != null) {
                        foodRecommendation.postValue(response.body())
                    } else {
                        Log.e(TAG, "onFailure: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<FoodRecommendationResponse>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }
            })
    }

    fun getExerciseRecommendations(
        token: String, recommendationRequest: RecommendationRequest,
        onResult: (ExerciseRecommendationResponse?) -> Unit) {
        ApiConfig.apiInstance
            .getExerciseRecommendations("Bearer $token", recommendationRequest)
            .enqueue(object : Callback<ExerciseRecommendationResponse> {
                override fun onResponse(
                    call: Call<ExerciseRecommendationResponse>,
                    response: Response<ExerciseRecommendationResponse>
                ) {
                    val responseBody = response.body()
                    onResult(responseBody)
                    if (responseBody != null) {
                        exerciseRecommendation.postValue(response.body())
                    } else {
                        Log.e(TAG, "onFailure: ${response.code()}")
                    }
                }
                override fun onFailure(call: Call<ExerciseRecommendationResponse>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }
            })
    }

    fun getUser(): LiveData<Account> {
        return pref.getUser().asLiveData()
    }

    companion object {
        const val TAG = "RecommendationViewModel"
        const val SUCCESS = "success"
    }
}