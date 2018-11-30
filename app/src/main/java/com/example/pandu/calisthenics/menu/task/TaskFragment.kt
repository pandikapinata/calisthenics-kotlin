package com.example.pandu.calisthenics.menu.task

import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import com.example.pandu.calisthenics.R
import com.example.pandu.calisthenics.api.APIClient
import com.example.pandu.calisthenics.db.database
import com.example.pandu.calisthenics.menu.detailTask.DetailTaskActivity
import com.example.pandu.calisthenics.model.ActivityItem
import com.example.pandu.calisthenics.model.Task
import com.example.pandu.calisthenics.utils.PreferenceHelper
import kotlinx.android.synthetic.main.fragment_activities.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.startActivity
import android.widget.ProgressBar
import android.widget.Toast
import com.example.pandu.calisthenics.utils.gone
import com.example.pandu.calisthenics.utils.isNetworkAvailable
import com.example.pandu.calisthenics.utils.visible
import org.jetbrains.anko.db.delete
import org.json.JSONObject
import org.json.JSONArray
import com.google.gson.Gson






@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class TaskFragment: Fragment(), TaskView {

    private var tasks : MutableList<Task> = mutableListOf()
    private lateinit var progressBar: ProgressBar
    private var preferencesHelper: PreferenceHelper? = null
    private lateinit var presenter: TaskPresenter
    private var jsonData: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       return inflater.inflate(R.layout.fragment_activities, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        progressBar = pb_activities
        setHasOptionsMenu(true)
        preferencesHelper = PreferenceHelper(activity)
        presenter = TaskPresenter(this, APIClient.getService(activity))

        if(isNetworkAvailable(context)){
            presenter.getTask()

        }else{
            getLocalData()
            hideLoading()
        }


        fab_create.setOnClickListener {
            context?.startActivity<CreateTaskActivity>()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_sync, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sync_data -> {
                syncData()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun syncData() {
        context?.database?.use {
            val result = select(Task.TABLE_TASK, "ID_ACTIVITY",
                "TASK_NOTE", "TASK_SETS", "TASK_REPS", "TASK_VOLUME", "TASK_DATE")
                .whereArgs("(STATUS = {status})",
                    "status" to "0")
            val taskSQLite = result.parseList(classParser<Task>())
            Log.e("BULK_INSERT", Gson().toJson(taskSQLite))
            jsonData = Gson().toJson(taskSQLite)
        }
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.gone()
    }


    override fun showTaskList(task: List<Task>) {

        Log.i("ISI TASK", task.toString())
//        context?.database?.use {
//            delete(Task.TABLE_TASK)
//        }
//        //insert to sqlite
//        try{
//            context?.database?.use{
//                for(value in task){
//                    insert(Task.TABLE_TASK,
////                        Task.TASK_ID to value.taskId,
////                        Task.USER_ID to value.user,
//                        Task.ID_ACTIVITY to value.activityId,
//                        Task.TASK_NAME to value.taskName,
//                        Task.TASK_NOTE to value.taskNote,
//                        Task.TASK_SETS to value.taskSets,
//                        Task.TASK_REPS to value.taskReps,
//                        Task.TASK_VOLUME to value.taskVolume,
//                        Task.TASK_DATE to value.taskDate,
//                        Task.TASK_ICON to value.taskIcon
//                    )
//                }
//            }
//
//        } catch (e: SQLiteConstraintException){
//            Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()
//        }

        getLocalData()


    }


    override fun showActivityList(typeAct: List<ActivityItem>) {

    }

    override fun storingTask() {

    }

    override fun onResume() {
        super.onResume()
        presenter.getTask()

    }


    override fun getLocalData() {
        tasks.clear()

        context?.database?.use {
            val result = select(Task.TABLE_TASK)
            val taskSQLite = result.parseList(classParser<Task>())
            tasks.addAll(taskSQLite)
            Log.i("BULK_INSERT", taskSQLite.toString())
        }

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_activities?.layoutManager = layoutManager

        rv_activities?.adapter = TaskAdapter(context, tasks){
            context?.startActivity<DetailTaskActivity>("task" to it)
        }

        (rv_activities.adapter as TaskAdapter).notifyDataSetChanged()

    }



}