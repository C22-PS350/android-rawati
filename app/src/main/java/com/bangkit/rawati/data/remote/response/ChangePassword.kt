package com.bangkit.rawati.data.remote.response

import com.google.gson.annotations.SerializedName

data class ChangePassword(

    @field:SerializedName("new_password")
    val new_password: String,

    @field:SerializedName("old_password")
    val old_password: String
)
