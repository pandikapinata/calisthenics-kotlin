package com.example.pandu.calisthenics.menu.task

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.pandu.calisthenics.R
import com.example.pandu.calisthenics.api.APIClient
import com.example.pandu.calisthenics.auth.AuthPresenter
import com.example.pandu.calisthenics.db.database
import com.example.pandu.calisthenics.model.ActivityItem
import com.example.pandu.calisthenics.model.Task
import kotlinx.android.synthetic.main.activity_create_task.*
import kotlinx.android.synthetic.main.fragment_activities.*
import org.jetbrains.anko.db.*
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS
import android.widget.DatePicker
import com.example.pandu.calisthenics.utils.*
import org.jetbrains.anko.toast


class CreateTaskActivity : AppCompatActivity(), TaskView {

    var cal = Calendar.getInstance()
    val year = cal.get(Calendar.YEAR)
    val month = cal.get(Calendar.MONTH)
    val day = cal.get(Calendar.DAY_OF_MONTH)


    private var menuItem: Menu? = null
    private var toolbarAct: Toolbar? = null
    private var preferencesHelper: PreferenceHelper? = null
    private lateinit var presenter: TaskPresenter
    var acts: MutableList<ActivityItem> = mutableListOf()
    private var actList: ActivityItem? = null
    lateinit var spinner: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)
        toolbarAct = toolbar
        spinner = spinner_create

        setSupportActionBar(toolbar)
        title = "Add activity"
        if (supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        preferencesHelper = PreferenceHelper(applicationContext)
        presenter = TaskPresenter(this, APIClient.getService(applicationContext))
        if (isNetworkAvailable(this)) {
            presenter.getActivities()
            getLocalData()
        } else {
            setSpinnerData()
        }
        getDateTime()


    }

    private fun getDateTime() {
        et_date_picker.setOnClickListener {
            val datePickerDialog =
                DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    // Display Selected date in textbox
//                et_date_picker.setText("$mYear-${mMonth+1}-$mDay")
                    cal.set(Calendar.YEAR, mYear)
                    cal.set(Calendar.MONTH, mMonth)
                    cal.set(Calendar.DAY_OF_MONTH, mDay)
                    et_date_picker.setText(calendarToRead(cal, "yyyy-MM-dd"))
                }, year, month, day)

            datePickerDialog.datePicker.minDate = Date().time - 1000
            datePickerDialog.show()
        }

        et_time_picker.setOnClickListener {
            val timePickerDialog = TimePickerDialog.OnTimeSetListener { view, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)

                et_time_picker.setText(SimpleDateFormat("HH:mm", Locale.ENGLISH).format(cal.time))
            }

            TimePickerDialog(
                this,
                timePickerDialog,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)
        menuItem = menu

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.create_task -> {
                storingTask()
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun responsesyncTask(tasks: List<Task>) {

    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun showTaskList(task: List<Task>) {

    }

    override fun getLocalData() {
        acts.clear()
        database.use {
            val result = select(ActivityItem.TABLE_ACTIVITY)
            val actLocal = result.parseList(classParser<ActivityItem>())
            acts.addAll(actLocal)
        }
    }

    override fun showActivityList(typeAct: List<ActivityItem>) {

//       acts.addAll(typeAct)
        database.use {
            delete(ActivityItem.TABLE_ACTIVITY)
        }
        //insert to sqlite
        try {
            database.use {
                for (value in typeAct) {
                    insert(
                        ActivityItem.TABLE_ACTIVITY,
                        ActivityItem.ACTIVITY_ID to value.activityId,
                        ActivityItem.ACTIVITY_NAME to value.activityName,
                        ActivityItem.ACTIVITY_ICON to value.activityIcon
                    )
                }
            }

        } catch (e: SQLiteConstraintException) {
            Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }

        setSpinnerData()
    }

    override fun responseTask(tasks: List<Task>) {
        Log.e("ID_PATOKAN", tasks.toString())
        database.use {
            insert(
                Task.TABLE_TASK,
                Task.TASK_ID to tasks[0].taskId,
                Task.ID_ACTIVITY to tasks[0].activityId,
                Task.TASK_NAME to tasks[0].taskName,
                Task.TASK_NOTE to tasks[0].taskNote,
                Task.TASK_SETS to tasks[0].taskSets,
                Task.TASK_REPS to tasks[0].taskReps,
                Task.TASK_VOLUME to tasks[0].taskVolume,
                Task.TASK_DATE to tasks[0].taskDate,
                Task.TASK_ICON to tasks[0].taskIcon,
                Task.STATUS_PUSH to "1",
                Task.STATUS_DELETE to "1"
            )
        }
        Toast.makeText(this, "Store Data Success", Toast.LENGTH_SHORT).show()
    }

    private fun storingTask() {
//        val date = et_date_picker.setText(calendarToString(cal, ""))
//        val time = et_time_picker.text.toString()

        val dateTime = calendarToString(cal, "yyyy-MM-dd HH:mm:ss")
        val volumeTask: Int = Integer.parseInt(et_sets.text.toString()) * Integer.parseInt(et_reps.text.toString())

        if (isNetworkAvailable(this)) {
            presenter.storeTask(
                actList?.activityId.toString(),
                et_notes.text.toString(),
                et_sets.text.toString(),
                et_reps.text.toString(),
                volumeTask.toString(),
                dateTime
            )
            presenter = TaskPresenter(this, APIClient.getService(this))
        } else {
            database.use {
               insert(
                    Task.TABLE_TASK,
                    Task.TASK_ID to "null",
                    Task.ID_ACTIVITY to actList?.activityId,
                    Task.TASK_NAME to actList?.activityName,
                    Task.TASK_NOTE to et_notes.text.toString(),
                    Task.TASK_SETS to et_sets.text.toString(),
                    Task.TASK_REPS to et_reps.text.toString(),
                    Task.TASK_VOLUME to volumeTask,
                    Task.TASK_DATE to dateTime,
                    Task.TASK_ICON to actList?.activityIcon,
                    Task.STATUS_PUSH to "0",
                    Task.STATUS_DELETE to "1"
                )
            }
            toast("Insert Off Sukses")
        }
    }

    private fun setSpinnerData() {
        val arrayAct: MutableList<String?> = mutableListOf()
        getLocalData()
        for (value in acts) {
            arrayAct.add(value.activityName)
        }
        val spinnerAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_dropdown_item, arrayAct)
        spinner_create.adapter = spinnerAdapter
        spinner_create.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                actList = acts[position]
//                activityId = actList?.activityId?.toInt()
            }
        }
    }
}
