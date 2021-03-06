package com.example.pandu.calisthenics.model

import com.google.gson.annotations.SerializedName

data class TaskResponse(
    @SerializedName("data")
    val tasks: List<Task>?
)