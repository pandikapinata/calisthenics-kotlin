package com.example.pandu.calisthenics.api

import com.example.pandu.calisthenics.model.*
import io.reactivex.Flowable
import retrofit2.Call
import retrofit2.http.*
import okhttp3.RequestBody
import okhttp3.MultipartBody
import retrofit2.http.POST
import retrofit2.http.Multipart



interface ApiInterface {
    @FormUrlEncoded
    @POST("auth/login")
    fun loginUser(@Field("email") email:String, @Field("password") password: String)
            : Call<AuthResponse>

    @FormUrlEncoded
    @POST("auth/signup")
    fun registerUser(
        @Field("name") name:String,
        @Field("email") email:String,
        @Field("password") password: String)
            : Call<AuthResponse>

    @GET("auth/user")
    fun userProfile()
            : Call<User>

    @GET("auth/tasks")
    fun getTaskUser() : Flowable<TaskResponse>

    @GET("auth/tasksDay")
    fun getTaskUserperDay() : Flowable<TaskDayResponse>

    @GET("auth/activities")
    fun getActivities() : Flowable<ActivityResponse>

    @GET("auth/totalVolume")
    fun getStatsDay() : Call<StatsResponse>

    @GET("auth/activity/{id}")
    fun getActivity(@Path("id") id: String) : Flowable<ActivityResponse>

    @FormUrlEncoded
    @POST("auth/task")
    fun storeTask(
        @Field("activity_id") activityId:String,
        @Field("note") note:String,
        @Field("sets") sets:String,
        @Field("reps") reps:String,
        @Field("volume") volume:String,
        @Field("date_task") dateTask:String)
            : Flowable<TaskResponse>

    @Multipart
    @POST("auth/updateUser")
    fun editProfile(
        @Part("name") name: RequestBody,
        @Part("weight") rentalPrice: RequestBody,
        @Part("height") description: RequestBody,
        @Part image: MultipartBody.Part?
        ): Call<User>

    @Multipart
    @POST("auth/pushToken")
    fun pushToken(
        @Part("fcm_token") name: RequestBody?
    )
            : Call<AuthResponse>
}