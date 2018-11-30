package com.example.pandu.calisthenics.menu.dashboard


import com.example.pandu.calisthenics.model.StatsResponse
import com.example.pandu.calisthenics.model.TaskDay

interface DashboardView {
    fun showLoading()
    fun hideLoading()
    fun getLocalData()
    fun getStatsData(stats: StatsResponse)
    fun showTaskList(taskDay: List<TaskDay>)
}