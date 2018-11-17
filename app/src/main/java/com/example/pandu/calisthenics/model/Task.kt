package com.example.pandu.calisthenics.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Task(
    @SerializedName("activity")
    val activity: Int? = 0,
    @SerializedName("id")
    val id: Int? = 0,
    @SerializedName("taskName")
    val taskName: String? = "",
    @SerializedName("taskNote")
    val taskNote: String? = "",
    @SerializedName("taskReps")
    val taskReps: Int? = 0,
    @SerializedName("taskSets")
    val taskSets: Int? = 0,
    @SerializedName("taskVolume")
    val taskVolume: Int? = 0,
    @SerializedName("user")
    val user: Int? = 0
):Parcelable