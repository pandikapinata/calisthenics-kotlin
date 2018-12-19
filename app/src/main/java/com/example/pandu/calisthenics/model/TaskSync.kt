package com.example.pandu.calisthenics.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TaskSync (
    val id: Long?,
    val taskId: String? = null,
    @SerializedName("activityId")
    val activityId: String? = null,
    @SerializedName("taskNote")
    val taskNote: String? = "",
    @SerializedName("taskSets")
    val taskSets: String? = null,
    @SerializedName("taskReps")
    val taskReps: String? = null,
    @SerializedName("taskVolume")
    val taskVolume: String? = null,
    @SerializedName("taskDate")
    val taskDate: String? = null
):Parcelable