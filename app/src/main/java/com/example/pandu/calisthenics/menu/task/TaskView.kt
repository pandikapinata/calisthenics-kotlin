package com.example.pandu.calisthenics.menu.task

import com.example.pandu.calisthenics.model.ActivityItem
import com.example.pandu.calisthenics.model.Task
import com.example.pandu.calisthenics.model.TaskResponse

interface TaskView {
    fun showLoading()
    fun hideLoading()
    fun getLocalData()
    fun showTaskList(task: List<Task>)
//    fun storingTask()
    fun responsesyncTask(tasks: List<Task>)
    fun showActivityList(typeAct : List<ActivityItem>)
    fun responseTask(tasks: List<Task>)
}