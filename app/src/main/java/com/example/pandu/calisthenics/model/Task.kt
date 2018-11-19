package com.example.pandu.calisthenics.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Task(
    @SerializedName("taskDate")
    val taskDate: String? = null,
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("taskName")
    val taskName: String? = "",
    @SerializedName("taskNote")
    val taskNote: String? = "",
    @SerializedName("taskReps")
    val taskReps: String? = null,
    @SerializedName("taskSets")
    val taskSets: String? = null,
    @SerializedName("taskVolume")
    val taskVolume: String? = null,
    @SerializedName("user")
    val user: String? = null,
    @SerializedName("activityId")
    val activityId: String? = null,
    @SerializedName("taskIcon")
    val taskIcon: String? = null
):Parcelable