package com.example.pandu.calisthenics.api

import com.example.pandu.calisthenics.model.AuthResponse
import com.example.pandu.calisthenics.model.User
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

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

    @FormUrlEncoded
    @POST("auth/user")
    fun userProfile()
            : Call<User>
}