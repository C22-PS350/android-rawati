package com.bangkit.rawati.data.remote.response

import com.google.gson.annotations.SerializedName

data class RegisterResult (
    @field:SerializedName("token")
    val token: String,

    @field:SerializedName("user_id")
    val id: Int
)