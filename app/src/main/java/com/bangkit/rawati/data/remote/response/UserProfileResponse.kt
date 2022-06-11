package com.bangkit.rawati.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserProfileResponse(
    @field:SerializedName("birth_date")
    val birth_date: String,

    @field:SerializedName("gender")
    val gender: String,

    @field:SerializedName("height")
    val height: Int,

    @field:SerializedName("weight")
    val weight: Int,

    @field:SerializedName("weight_goal")
    val weight_goal: Int,

    @field:SerializedName("data")
    val userProfileResult: UserProfileResult?
)

data class UserProfileResult(
    @field:SerializedName("profile_id")
    val profile_id: String,

    @field:SerializedName("user_id")
    val user_id: String,

    @field:SerializedName("height")
    val height: String,

    @field:SerializedName("weight")
    val weight: String,

    @field:SerializedName("weight_goal")
    val weight_goal: String,

    @field:SerializedName("gender")
    val gender: String,

    @field:SerializedName("birth_date")
    val birth_date: String
)
