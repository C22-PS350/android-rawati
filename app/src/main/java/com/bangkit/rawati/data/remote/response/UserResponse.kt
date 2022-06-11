package com.bangkit.rawati.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @field:SerializedName("data")
    val userResult: UserResult?
)

data class UserResult(
    @field:SerializedName("user_id")
    val user_id: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("is_verified")
    val is_verified: String
)
