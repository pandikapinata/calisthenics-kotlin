package com.example.pandu.calisthenics.api

import com.example.pandu.calisthenics.model.ActivityResponse
import com.example.pandu.calisthenics.model.AuthResponse
import com.example.pandu.calisthenics.model.TaskResponse
import com.example.pandu.calisthenics.model.User
import io.reactivex.Flowable
import retrofit2.Call
import retrofit2.http.*

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

    @GET("auth/activities")
    fun getActivities() : Flowable<ActivityResponse>

    @GET("auth/activity/{id}")
    fun getActivity(@Path("id") id: String) : Flowable<ActivityResponse>

    @FormUrlEncoded
    @POST("auth/task")
    fun storeTask(
        @Field("activity_id") activityId:String,
        @Field("note") note:String,
        @Field("sets") sets:String,
        @Field("reps") reps:String,
        @Field("date_task") dateTask:String)
            : Flowable<TaskResponse>
}