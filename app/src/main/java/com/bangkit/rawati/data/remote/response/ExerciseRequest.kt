package com.bangkit.rawati.data.remote.response

import com.google.gson.annotations.SerializedName

// For POST Exercise to Endpoint
data class ExerciseRequest(
    @field:SerializedName("body_temp")
    val body_temp: Float,

    @field:SerializedName("duration")
    val duration: Int,

    @field:SerializedName("heart_rate")
    val heart_rate: Int,

    @field:SerializedName("name")
    val name: String,
)

data class ExerciseResponse(
    @field:SerializedName("exercise_activity_id")
    val food_activity_id: Int,

    @field:SerializedName("user_id")
    val user_id: Int
)

// GET All Exercise Activity
data class AllExerciseActivityRequest(
    @field:SerializedName("data")
    val exerciseActivityData: List<ExerciseActivityData>?
)

// GET Detail Exercise Activity
data class ExerciseActivityRequest(
    @field:SerializedName("data")
    val exerciseActivityData: ExerciseActivityData?
)

data class ExerciseActivityData(
    @field:SerializedName("calories")
    val calories: Float,

    @field:SerializedName("duration")
    val duration: Int,

    @field:SerializedName("exercise_activity_id")
    val exercise_activity_id: Int,

    @field:SerializedName("exercise_date")
    val exercise_date: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("user_id")
    val user_id: Int
)
