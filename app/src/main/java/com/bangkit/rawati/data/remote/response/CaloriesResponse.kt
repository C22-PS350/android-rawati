package com.bangkit.rawati.data.remote.response

import com.google.gson.annotations.SerializedName

data class CaloriesResponse(
    @field:SerializedName("data")
    val caloriesData: CaloriesData?
)

data class CaloriesData(
    @field:SerializedName("exercise_total")
    val exercise: Float,

    @field:SerializedName("food_total")
    val food: Float,

    @field:SerializedName("user_id")
    val user_id: Int
)
