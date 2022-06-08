package com.bangkit.rawati.data.remote.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("password")
    val password: String,

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("data")
    val registerResult: RegisterResult?
)