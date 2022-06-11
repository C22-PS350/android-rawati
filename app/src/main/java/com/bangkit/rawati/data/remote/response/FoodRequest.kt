package com.bangkit.rawati.data.remote.response

import com.google.gson.annotations.SerializedName

// For POST Food to Endpoint
data class FoodRequest(
    @field:SerializedName("calories")
    val calories: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("data")
    val registerResult: FoodResponse?
)

data class FoodResponse(
    @field:SerializedName("food_activity_id")
    val food_activity_id: Int,

    @field:SerializedName("user_id")
    val user_id: Int
)

// GET All Food Activity
data class AllFoodActivityRequest(
    @field:SerializedName("data")
    val foodActivityData: List<FoodActivityData>?
)

// GET Detail Food Activity
data class FoodActivityRequest(
    @field:SerializedName("data")
    val foodActivityData: FoodActivityData?
)

data class FoodActivityData(
    @field:SerializedName("calories")
    val calories: Int,

    @field:SerializedName("food_activity_id")
    val food_activity_id: Int,

    @field:SerializedName("food_date")
    val food_date: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("user_id")
    val user_id: Int
)