package com.bangkit.rawati.data.remote.api

import com.bangkit.rawati.data.remote.response.ApiResponse
import com.bangkit.rawati.data.remote.response.LoginResponse
import com.bangkit.rawati.data.remote.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.*


interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("auth/register")
    fun register(
        @Body registerResponse: RegisterResponse
    ): Call<RegisterResponse>

    @Headers("Content-Type: application/json")
    @POST("auth/login")
    fun login(
        @Body loginResponse: LoginResponse
    ): Call<LoginResponse>

    /*@Headers("Content-Type: application/json")
    @PUT("auth/forgot-password")
    fun forgotPassword(
        @Body apiResponse: ApiResponse
    ): Call<ApiResponse>*/
}