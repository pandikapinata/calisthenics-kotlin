package com.example.pandu.calisthenics.menu.task

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pandu.calisthenics.R
import com.example.pandu.calisthenics.api.APIClient
import com.example.pandu.calisthenics.menu.detailTask.DetailTaskActivity
import com.example.pandu.calisthenics.model.ActivityItem
import com.example.pandu.calisthenics.model.Task
import com.example.pandu.calisthenics.utils.PreferenceHelper
import com.example.pandu.calisthenics.utils.gone
import com.example.pandu.calisthenics.utils.visible
import kotlinx.android.synthetic.main.fragment_activities.*
import kotlinx.android.synthetic.main.task_list.*
import org.jetbrains.anko.startActivity

class TaskFragment: Fragment(), TaskView {

    private var tasks : MutableList<Task> = mutableListOf()

    private var preferencesHelper: PreferenceHelper? = null
    private lateinit var presenter: TaskPresenter
    var rootView : View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_activities, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        preferencesHelper = PreferenceHelper(activity)
        presenter = TaskPresenter(this, APIClient.getService(activity))
        presenter.getTask()
        sr_activities.setOnRefreshListener {
            sr_activities.isRefreshing = false
            presenter.getTask()
        }

        fab_create.setOnClickListener {
            context?.startActivity<CreateTaskActivity>()
        }

    }

    override fun showLoading() {
        pb_activities.visible()
    }

    override fun hideLoading() {
        pb_activities.gone()
    }

    override fun showTaskList(task: List<Task>) {
        tasks.clear()
        tasks.addAll(task)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_activities.layoutManager = layoutManager
        rv_activities.adapter = TaskAdapter(tasks){
            context?.startActivity<DetailTaskActivity>("task" to it)
        }
    }

    override fun showActivityList(typeAct: List<ActivityItem>) {

    }

    override fun storingTask() {

    }

    override fun onResume() {
        super.onResume()
        presenter.getTask()
    }

}