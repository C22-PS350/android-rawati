package com.bangkit.rawati.data.remote.response

import com.google.gson.annotations.SerializedName

// GET Exercise Recommendations
data class ExerciseRecommendationResponse(
    @field:SerializedName("data")
    val exerciseReccomendation: List<ExerciseRecommendationData>?
)

data class ExerciseRecommendationData(
    @field:SerializedName("exercise_id")
    val exercise_id: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("calories")
    val calories: Int,
)

// GET Food Recommendations
data class FoodRecommendationResponse(
    @field:SerializedName("data")
    val foodReccomendation: List<FoodRecommendationData>?
)

data class FoodRecommendationData(
    @field:SerializedName("food_id")
    val food_id: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("calories")
    val calories: Int,
)