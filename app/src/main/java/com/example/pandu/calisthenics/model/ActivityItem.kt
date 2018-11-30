package com.example.pandu.calisthenics.model

import com.google.gson.annotations.SerializedName

data class ActivityItem(
    val id: Long?,
    val activityId: String? = "",
    val activityName: String? = "",
    val activityIcon: String? = ""
){
    companion object {
        const val TABLE_ACTIVITY : String = "TABLE_ACTIVITY"
        const val ID : String = "ID_"
        const val ACTIVITY_ID : String = "ACTIVITY_ID"
        const val ACTIVITY_NAME : String = "ACTIVITY_NAME"
        const val ACTIVITY_ICON : String = "ACTIVITY_ICON"
    }
}