package com.bangkit.rawati.ui.activity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bangkit.rawati.data.local.datastore.Account
import com.bangkit.rawati.data.local.datastore.AccountPreferences
import com.bangkit.rawati.data.remote.api.ApiConfig
import com.bangkit.rawati.data.remote.response.*
import com.bangkit.rawati.helper.ApiCallbackString
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ActivityViewModel (private val pref: AccountPreferences): ViewModel(){

    // Date Format: yyyy-MM-dd
    //val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private var currentDate: Calendar = Calendar.getInstance()
    val foods = MutableLiveData<AllFoodActivityRequest>()
    val exercises = MutableLiveData<AllExerciseActivityRequest>()
    val food = MutableLiveData<FoodActivityRequest>()
    val exercise = MutableLiveData<ExerciseActivityRequest>()
    private val iso8601Date: DateFormat = SimpleDateFormat("yyyy-MM-dd")

    fun getDate(): Date {
        return currentDate.time
    }

    /**
     * Get today's date
     */
    fun todayDate(): Date {
        val todayCal = Calendar.getInstance()
        return todayCal.time
    }
    /**
     * Get yesterday's date
     */
    fun yesterdayDate(): Date {
        val todayCal = Calendar.getInstance()
        todayCal.add(Calendar.DATE, -1)
        return todayCal.time
    }

    /**
     * Change the current date to previous date
     */
    fun prevDate(): Date{
        currentDate.add(Calendar.DATE, -1)
        return getDate()
    }

    /**
     * Change the date to next date
     * only if it's not more than today's date
     */
    fun nextDate(): Date {
        if (iso8601Date.format(currentDate.time) != iso8601Date.format(todayDate())) currentDate.add(Calendar.DATE, +1)
        return getDate()
    }

    /**
     * Change the date to specific date
     */
    fun changeDate() {

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
                        Log.e(TAG, "onFailure: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<AllExerciseActivityRequest>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }
            })
    }
    fun getExerciseDetail(
        token: String, user_id: String, exercise_id: String,
        onResult: (ExerciseActivityRequest?) -> Unit) {

        ApiConfig.apiInstance
            .getExerciseDetail("Bearer $token", user_id, exercise_id)
            .enqueue(object : Callback<ExerciseActivityRequest>{
                override fun onResponse(
                    call: Call<ExerciseActivityRequest>,
                    response: Response<ExerciseActivityRequest>
                ) {
                    val responseBody = response.body()
                    onResult(responseBody)
                    if (responseBody != null) {
                        exercise.postValue(response.body())
                    } else {
                        Log.e(TAG, "onFailure: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ExerciseActivityRequest>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }
            })
    }

    //TODO(Astrada): Change create exercise activity function
    fun createExerciseActivity() {

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
                        Log.e(TAG, "onFailure: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<AllFoodActivityRequest>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }
            })
    }
    fun getFoodDetail(
        token: String, user_id: String, food_id: String,
        onResult: (FoodActivityRequest?) -> Unit) {
        ApiConfig.apiInstance
            .getFoodDetail("Bearer $token", user_id, food_id)
            .enqueue(object : Callback<FoodActivityRequest> {
                override fun onResponse(
                    call: Call<FoodActivityRequest>,
                    response: Response<FoodActivityRequest>
                ) {
                    val responseBody = response.body()
                    onResult(responseBody)
                    if (responseBody != null) {
                        food.postValue(response.body())
                    } else {
                        Log.e(TAG, "onFailure: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<FoodActivityRequest>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }
            })
    }

    fun createFoodActivity(
        token: String, user_id: String, foodRequest: FoodRequest,
        param: ApiCallbackString, onResult: (FoodRequest?) -> Unit) {
        ApiConfig.apiInstance
            .createFoodActivity("Bearer $token", user_id, foodRequest)
            .enqueue(object : Callback<FoodRequest> {
                override fun onResponse(
                    call: Call<FoodRequest>,
                    response: Response<FoodRequest>
                ) {
                    val responseBody = response.body()
                    onResult(responseBody)
                    if (responseBody != null) {
                        param.onResponse(responseBody != null, SUCCESS)
                    } else {
                        Log.e(TAG, "onFailure: ${response.code()}")
                        val jsonObject =
                            JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
                        val message = jsonObject.getString("error")
                        param.onResponse(false, message)
                    }
                }

                override fun onFailure(call: Call<FoodRequest>, t: Throwable) {
                    onResult(null)
                }
            })
    }

    fun getUser(): LiveData<Account> {
        return pref.getUser().asLiveData()
    }

    companion object {
        const val TAG = "ActivityViewModel"
        const val SUCCESS = "success"
    }
}
