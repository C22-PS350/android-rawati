package com.bangkit.rawati.data.remote.api

import com.bangkit.rawati.data.remote.response.ApiResponse
import com.bangkit.rawati.data.remote.response.LoginResponse
import retrofit2.Call
import retrofit2.http.*


interface ApiService {
    @FormUrlEncoded
    @POST("auth/register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") pass: String
    ): Call<ApiResponse>

    @Headers("Content-Type: application/json")
    @POST("auth/login")
    fun login(
        @Body loginResponse: LoginResponse
    ): Call<LoginResponse>
}