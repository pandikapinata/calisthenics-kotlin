package com.example.pandu.calisthenics.menu.task

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
import org.jetbrains.anko.startActivity
import android.widget.ProgressBar
import android.widget.Toast
import com.example.pandu.calisthenics.model.TaskSync
import com.example.pandu.calisthenics.model.TaskSyncSend
import com.example.pandu.calisthenics.utils.gone
import com.example.pandu.calisthenics.utils.isNetworkAvailable
import com.example.pandu.calisthenics.utils.visible
import com.google.gson.Gson
import org.jetbrains.anko.db.*


class TaskFragment: Fragment(), TaskView {

    private var tasks : MutableList<Task> = mutableListOf()
    private lateinit var progressBar: ProgressBar
    private var preferencesHelper: PreferenceHelper? = null
    private lateinit var presenter: TaskPresenter
    private var adapter: TaskAdapter? = null
    private var taskSync: TaskSyncSend? = null
    private var rvTask : RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       return inflater.inflate(R.layout.fragment_activities, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        progressBar = pb_activities
        rvTask = rv_activities
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar!!.title = "Activities"
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
                syncTask()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun responsesyncTask(tasks: List<Task>) {
        context?.database?.use {
            for(value in tasks){
                update(Task.TABLE_TASK,
                Task.TASK_ID to value.taskId,
                    Task.ID_ACTIVITY to value.activityId,
                    Task.TASK_NAME to value.taskName,
                    Task.TASK_NOTE to value.taskNote,
                    Task.TASK_SETS to value.taskSets,
                    Task.TASK_REPS to value.taskReps,
                    Task.TASK_VOLUME to value.taskVolume,
                    Task.TASK_DATE to value.taskDate,
                    Task.TASK_ICON to value.taskIcon,
                    Task.STATUS_PUSH to "1",
                    Task.STATUS_DELETE to "1")
                    .whereArgs("ID_ = {idInsert}", "idInsert" to value.id.toString())
                    .exec()
            }
        }
        Toast.makeText(context, "Success Sync", Toast.LENGTH_SHORT).show()
    }

    private fun syncTask() {

        if(isNetworkAvailable(context)){
            context?.database?.use {
                val result = select(Task.TABLE_TASK, "ID_","TASK_ID", "ID_ACTIVITY",
                    "TASK_NOTE", "TASK_SETS", "TASK_REPS", "TASK_VOLUME", "TASK_DATE")
                    .whereArgs("STATUS_PUSH = {status}",
                        "status" to "0")
                val taskSQLite = result.parseList(classParser<TaskSync>())

                if (!taskSQLite.isEmpty()) {
                    Log.e("BULK_INSERT", taskSQLite.toString())
                    taskSync = TaskSyncSend(taskSQLite)
                    Log.e("TASK_SYNC", taskSync.toString())
                    presenter = TaskPresenter(this@TaskFragment, APIClient.getService(context))
                    presenter.syncTasks(taskSync!!)

                }
                else{
                    Toast.makeText(context, "Already Synced", Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            Toast.makeText(context, "No Internet Connection!", Toast.LENGTH_SHORT).show()
        }



    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.gone()
    }


    override fun showTaskList(task: List<Task>) {
//        context?.database?.use {
//            delete(Task.TABLE_TASK)
//        }
//        //insert to sqlite
//        try{
//            context?.database?.use{
//                for(value in task){
//                    insert(Task.TABLE_TASK,
//                        Task.TASK_ID to value.taskId,
//                        Task.USER_ID to value.user,
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

    override fun responseTask(tasks: List<Task>) {

    }

    override fun onResume() {
        super.onResume()
        presenter.getTask()
    }



    override fun getLocalData() {
        tasks.clear()

        context?.database?.use {
            val result = select(Task.TABLE_TASK)
                .orderBy("TASK_DATE", SqlOrderDirection.ASC)
//                .orderBy("TASK_DATE", SqlOrderDirection.DESC)
                .whereArgs("(STATUS_DELETE = {status})",
                    "status" to "1")
            val taskSQLite  = result.parseList(classParser<Task>())
            tasks.addAll(taskSQLite)
            Log.e("LOCAL_DATA", taskSQLite.toString())
        }

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvTask?.layoutManager = layoutManager

        adapter = TaskAdapter(context, tasks){
            context?.startActivity<DetailTaskActivity>("task" to it)
        }
        rvTask?.adapter = adapter
        adapter?.notifyDataSetChanged()

    }



}