package com.example.pandu.calisthenics.model

import com.google.gson.annotations.SerializedName

data class TaskDayResponse(
    @SerializedName("data")
    val tasks: List<TaskDay>?
)