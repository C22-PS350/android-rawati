package com.bangkit.rawati.data.remote.api

import com.bangkit.rawati.data.remote.response.*
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

    @Headers("Content-Type: application/json")
    @PUT("auth/forgot-password")
    fun resetPassword(
        @Body resetPassword: ResetPassword
    ): Call<ResetPassword>

    @Headers("Content-Type: application/json")
    @PUT("users/{user_id}/update-password")
    fun changePassword(
        @Header("Authorization") token: String,
        @Path("user_id") user_id: String,
        @Body changePassword: ChangePassword
    ): Call<ChangePassword>

    @Headers("Content-Type: application/json")
    @GET("users/{user_id}")
    fun getDataUser(
        @Header("Authorization") token: String,
        @Path("user_id") user_id: String
    ): Call<UserResponse>

    @Headers("Content-Type: application/json")
    @PUT("users/{user_id}/profile")
    fun updateProfile(
        @Header("Authorization") token: String,
        @Path("user_id") user_id: String,
        @Body userProfileResponse: UserProfileResponse
    ): Call<UserProfileResponse>

    @Headers("Content-Type: application/json")
    @GET("users/{user_id}/profile")
    fun getProfile(
        @Header("Authorization") token: String,
        @Path("user_id") user_id: String
    ): Call<UserProfileResponse>
}