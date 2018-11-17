package com.example.pandu.calisthenics.model

import com.google.gson.annotations.SerializedName

data class TaskResponse(
    @SerializedName("data")
    val `data`: List<Task>?
)