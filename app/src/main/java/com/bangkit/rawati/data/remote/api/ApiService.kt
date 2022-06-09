package com.bangkit.rawati.data.remote.api

import com.bangkit.rawati.data.remote.response.LoginResponse
import com.bangkit.rawati.data.remote.response.RegisterResponse
import com.bangkit.rawati.data.remote.response.ResetPassword
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT


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

    @Headers("Content-Type: application/json")
    @PUT("auth/forgot-password")
    fun resetPassword(
        @Body resetPassword: ResetPassword
    ): Call<ResetPassword>
}