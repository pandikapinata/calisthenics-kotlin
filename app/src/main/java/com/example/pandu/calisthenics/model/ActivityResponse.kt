package com.example.pandu.calisthenics.model

import com.google.gson.annotations.SerializedName

data class ActivityResponse(
    @SerializedName("data")
    val listActivities: List<ActivityItem>?
)