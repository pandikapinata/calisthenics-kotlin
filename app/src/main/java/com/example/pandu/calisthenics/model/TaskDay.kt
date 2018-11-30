package com.example.pandu.calisthenics.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TaskDay(
    val id: Long?,
    @SerializedName("taskId")
    val taskId: String? = null,
    @SerializedName("user")
    val user: String? = null,
    @SerializedName("activityId")
    val activityId: String? = null,
    @SerializedName("taskName")
    val taskName: String? = "",
    @SerializedName("taskNote")
    val taskNote: String? = "",
    @SerializedName("taskSets")
    val taskSets: String? = null,
    @SerializedName("taskReps")
    val taskReps: String? = null,
    @SerializedName("taskVolume")
    val taskVolume: String? = null,
    @SerializedName("taskDate")
    val taskDate: String? = null,
    @SerializedName("taskIcon")
    val taskIcon: String? = null
):Parcelable{

    companion object {
        const val TABLE_TASKDAY : String = "TABLE_TASKDAY"
        const val ID : String = "ID_"
        const val TASK_ID : String = "TASK_ID"
        const val USER_ID : String = "USER_ID"
        const val ID_ACTIVITY : String = "ID_ACTIVITY"
        const val TASK_NAME : String = "TASK_NAME"
        const val TASK_NOTE : String = "TASK_NOTE"
        const val TASK_SETS : String = "TASK_SETS"
        const val TASK_REPS : String = "TASK_REPS"
        const val TASK_VOLUME : String = "TASK_VOLUME"
        const val TASK_DATE : String = "TASK_DATE"
        const val TASK_ICON : String = "TASK_ICON"
    }
}