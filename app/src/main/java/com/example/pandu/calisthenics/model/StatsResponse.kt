package com.example.pandu.calisthenics.model


data class StatsResponse(
    val id: Long,
    var sets: String? = null,
    var volume: String? = null
){
    companion object {
        const val TABLE_STATS : String = "TABLE_STATS"
        const val ID : String = "ID_"
        const val STAT_SETS : String = "STAT_SETS"
        const val STAT_VOLUME : String = "STAT_VOLUME"
    }
}