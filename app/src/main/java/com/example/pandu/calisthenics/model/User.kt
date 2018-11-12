package com.example.pandu.calisthenics.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("avatar")
    val avatar: String? = "",
    @SerializedName("avatar_url")
    val avatarUrl: String? = "",
    @SerializedName("email")
    val email: String? = "",
    @SerializedName("id")
    val id: Int? = 0,
    @SerializedName("name")
    val name: String? = ""

)