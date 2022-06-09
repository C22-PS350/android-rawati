package com.bangkit.rawati.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("identifier")
    val identifier: String,

    @field:SerializedName("password")
    val password: String,

    @field:SerializedName("data")
    val loginResult: LoginResult?
)

data class ResetPassword(
    @field:SerializedName("email")
    val email: String
)
