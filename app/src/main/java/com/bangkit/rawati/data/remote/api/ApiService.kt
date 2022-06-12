package com.bangkit.rawati.data.remote.api

import com.bangkit.rawati.data.remote.response.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    // Auth Endpoints
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


    // User Endpoints
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


    // Activity (Exercise) Endpoints
    @Headers("Content-Type: application/json")
    @GET("users/{user_id}/exercises")
    fun getExerciseActivity(
        @Header("Authorization") token: String,
        @Path("user_id") user_id: String,
        @Query("date") date: String
    ): Call<AllExerciseActivityRequest>

    //TODO(Astrada): Add POST Exercise

    @Headers("Content-Type: application/json")
    @GET("users/{user_id}/exercises/{exercise_id}")
    fun getExerciseDetail(
        @Header("Authorization") token: String,
        @Path("user_id") user_id: String,
        @Path("exercise_id") exercise_id: String
    ): Call<ExerciseActivityRequest>


    // Activity (Food) Endpoints
    @Headers("Content-Type: application/json")
    @GET("users/{user_id}/foods")
    fun getFoodActivity(
        @Header("Authorization") token: String,
        @Path("user_id") user_id: String,
        @Query("date") date: String
    ): Call<AllFoodActivityRequest>

    @Headers("Content-Type: application/json")
    @POST("users/{user_id}/foods")
    fun createFoodActivity(
        @Header("Authorization") token: String,
        @Path("user_id") user_id: String,
        @Body foodRequest: FoodRequest
    ): Call<FoodRequest>

    @Headers("Content-Type: application/json")
    @GET("users/{user_id}/foods/{food_id}")
    fun getFoodDetail(
        @Header("Authorization") token: String,
        @Path("user_id") user_id: String,
        @Path("food_id") food_id: String
    ): Call<FoodActivityRequest>


    // Profile Endpoints
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


    // Recommendation Endpoints
    @Headers("Content-Type: application/json")
    @POST("recommendation/exercise")
    fun getExerciseRecommendations(
        @Header("Authorization") token: String,
        @Body calories: Int
    ): Call<ExerciseRecommendationResponse>

    @Headers("Content-Type: application/json")
    @POST("recommendation/food")
    fun getFoodRecommendations(
        @Header("Authorization") token: String,
        @Body calories: Int
    ): Call<FoodRecommendationResponse>
}
