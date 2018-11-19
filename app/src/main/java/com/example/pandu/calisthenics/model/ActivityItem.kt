package com.example.pandu.calisthenics.model

import com.google.gson.annotations.SerializedName

data class ActivityItem(
    @SerializedName("activityName")
    val activityName: String? = "",
    @SerializedName("id")
    val id: Int? = 0
)