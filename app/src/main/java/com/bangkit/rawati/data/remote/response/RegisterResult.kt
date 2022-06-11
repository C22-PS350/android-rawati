package com.bangkit.rawati.data.remote.response

import com.google.gson.annotations.SerializedName

data class RegisterResult (
    @field:SerializedName("user_id")
    val user_id: String,

    @field:SerializedName("token")
    val token: String
)