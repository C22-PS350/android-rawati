package com.bangkit.rawati.data.remote.response

import com.google.gson.annotations.SerializedName

data class RecommendationRequest(
    @field:SerializedName("calories")
    val calories: Int
)

// POST Exercise Recommendations
data class ExerciseRecommendationResponse(
    @field:SerializedName("data")
    val exerciseRecommendation: List<ExerciseRecommendationData>?
)

data class ExerciseRecommendationData(
    @field:SerializedName("exercise_id")
    val exercise_id: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("calories")
    val calories: Float,
)

// POST Food Recommendations
data class FoodRecommendationResponse(
    @field:SerializedName("data")
    val foodRecommendation: List<FoodRecommendationData>?
)

data class FoodRecommendationData(
    @field:SerializedName("food_id")
    val food_id: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("calories")
    val calories: Float,
)