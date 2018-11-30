package com.example.pandu.calisthenics.menu.task

import com.example.pandu.calisthenics.model.ActivityItem
import com.example.pandu.calisthenics.model.Task

interface TaskView {
    fun showLoading()
    fun hideLoading()
    fun getLocalData()
    fun showTaskList(task: List<Task>)
    fun storingTask()
    fun showActivityList(typeAct : List<ActivityItem>)
}