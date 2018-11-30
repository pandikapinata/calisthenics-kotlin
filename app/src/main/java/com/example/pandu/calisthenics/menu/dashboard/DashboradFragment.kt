package com.example.pandu.calisthenics.menu.dashboard

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.pandu.calisthenics.R
import com.example.pandu.calisthenics.api.APIClient
import com.example.pandu.calisthenics.db.database
import com.example.pandu.calisthenics.menu.detailTask.DetailTaskActivity
import com.example.pandu.calisthenics.menu.task.TaskAdapter
import com.example.pandu.calisthenics.model.StatsResponse
import com.example.pandu.calisthenics.model.Task
import com.example.pandu.calisthenics.model.TaskDay
import com.example.pandu.calisthenics.utils.PreferenceHelper
import com.example.pandu.calisthenics.utils.isNetworkAvailable
import kotlinx.android.synthetic.main.fragment_dashboard.*
import org.jetbrains.anko.db.*
import org.jetbrains.anko.startActivity

class DashboradFragment: Fragment(), DashboardView {

    private var tasksDay : MutableList<TaskDay> = mutableListOf()
    private var preferencesHelper: PreferenceHelper? = null
    private lateinit var presenter: DashboardPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        preferencesHelper = PreferenceHelper(activity)
        presenter = DashboardPresenter(this, APIClient.getService(activity))

        if(isNetworkAvailable(context)){
            showLoading()
            presenter.getTaskperDay()
            presenter.getStatsperDay()
        }else{
            getLocalData()
            hideLoading()
        }
    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun getLocalData() {
        tasksDay.clear()

        context?.database?.use {
            val result = select(TaskDay.TABLE_TASKDAY)
            val taskSQLite = result.parseList(classParser<TaskDay>())
            tasksDay.addAll(taskSQLite)
        }
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_today_activities?.layoutManager = layoutManager

        rv_today_activities?.adapter = DashboardAdapter(context, tasksDay){
            context?.startActivity<DetailTaskActivity>("task" to it)
        }

        (rv_today_activities.adapter as DashboardAdapter).notifyDataSetChanged()
    }

    override fun showTaskList(taskDay: List<TaskDay>) {
        Log.i("ISI TASK DAY", taskDay.toString())
        context?.database?.use {
            delete(TaskDay.TABLE_TASKDAY)
        }
        //insert to sqlite
        try{
            context?.database?.use{
                for(value in taskDay){
                    insert(TaskDay.TABLE_TASKDAY,
                        TaskDay.TASK_ID to value.taskId,
                        TaskDay.USER_ID to value.user,
                        TaskDay.ID_ACTIVITY to value.activityId,
                        TaskDay.TASK_NAME to value.taskName,
                        TaskDay.TASK_NOTE to value.taskNote,
                        TaskDay.TASK_SETS to value.taskSets,
                        TaskDay.TASK_REPS to value.taskReps,
                        TaskDay.TASK_VOLUME to value.taskVolume,
                        TaskDay.TASK_DATE to value.taskDate,
                        TaskDay.TASK_ICON to value.taskIcon
                    )
                }
            }

        } catch (e: SQLiteConstraintException){
            Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }

        getLocalData()

    }

    override fun getStatsData(stats: StatsResponse) {
        tv_total_sets.text = stats.sets
        tv_total_volume.text = stats.volume
    }
}