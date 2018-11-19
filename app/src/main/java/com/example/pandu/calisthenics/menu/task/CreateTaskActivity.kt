package com.example.pandu.calisthenics.menu.task

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.pandu.calisthenics.R
import com.example.pandu.calisthenics.api.APIClient
import com.example.pandu.calisthenics.auth.AuthPresenter
import com.example.pandu.calisthenics.model.ActivityItem
import com.example.pandu.calisthenics.model.Task
import com.example.pandu.calisthenics.utils.PreferenceHelper
import kotlinx.android.synthetic.main.activity_create_task.*
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS

class CreateTaskActivity : AppCompatActivity(), TaskView {

    var cal = Calendar.getInstance()
    val year = cal.get(Calendar.YEAR)
    val month = cal.get(Calendar.MONTH)
    val day = cal.get(Calendar.DAY_OF_MONTH)
    private var menuItem: Menu? = null
    private var toolbarAct: Toolbar? = null
    private var activityId: Int? = null
    private var preferencesHelper: PreferenceHelper? = null
    private lateinit var presenter: TaskPresenter
    var acts : MutableList<ActivityItem> = mutableListOf()
    lateinit var spinner : Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)
        toolbarAct = toolbar
        spinner = spinner_create
        setSupportActionBar(toolbar)
        if (supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        preferencesHelper = PreferenceHelper(applicationContext)
        presenter = TaskPresenter(this, APIClient.getService(applicationContext))
        presenter.getActivities()
        et_date_picker.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                // Display Selected date in textbox

                et_date_picker.setText("$mYear-${mMonth+1}-$mDay")
            }, year, month, day)

            datePickerDialog.datePicker.maxDate = cal.timeInMillis
            datePickerDialog.show()
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
//                val intent = Intent(applicationContext, TaskFragment::class.java)
//                this.startActivity(intent)
//                this.finish()
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun showTaskList(task: List<Task>) {

    }

    override fun showActivityList(typeAct: List<ActivityItem>) {
        acts.clear()
        acts.addAll(typeAct)
        setData()
    }

    override fun storingTask() {
        presenter?.storeTask(
            activityId.toString(),
            et_notes.text.toString(),
            et_sets.text.toString(),
            et_reps.text.toString(),
            et_date_picker.text.toString())
        presenter = TaskPresenter(this, APIClient.getService(this))
        Toast.makeText(this, "Store Data Success", Toast.LENGTH_SHORT).show()
    }

    private fun storeData(){
        presenter?.storeTask(
            activityId.toString(),
            et_notes.text.toString(),
            et_sets.text.toString(),
            et_reps.text.toString(),
            et_date_picker.text.toString())
        presenter = TaskPresenter(this, APIClient.getService(this))
    }
    private fun setData() {
        val arrayAct : MutableList<String?> = mutableListOf()
        for((index,value) in acts.withIndex()){
            arrayAct.add(value.activityName)
        }
        val spinnerAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_dropdown_item, arrayAct)
        spinner_create.adapter = spinnerAdapter
        spinner_create.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val actList = acts[position]
                activityId = actList.id

            }
        }
    }
}
