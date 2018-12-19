package com.example.pandu.calisthenics.menu.detailTask

import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.example.pandu.calisthenics.R
import com.example.pandu.calisthenics.model.Task
import com.example.pandu.calisthenics.utils.toDatetoString
import kotlinx.android.synthetic.main.activity_detail_task.*
import org.jetbrains.anko.*
import android.support.v7.app.AlertDialog
import android.widget.TextView
import android.widget.Toast
import com.example.pandu.calisthenics.api.APIClient
import com.example.pandu.calisthenics.db.database
import com.example.pandu.calisthenics.menu.task.EditTaskActivity
import com.example.pandu.calisthenics.model.BasicResponse
import com.example.pandu.calisthenics.model.User
import okhttp3.RequestBody
import org.jetbrains.anko.db.update
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailTaskActivity : AppCompatActivity() {

    private var toolbarAct: Toolbar? = null
    private lateinit var task : Task
    private var menuItem: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_task)
        task = intent.getParcelableExtra("task")
        toolbarAct = toolbar_detail
        setSupportActionBar(toolbar_detail)
        toolbar_layout.title = task.taskName
        if (supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        setData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_task, menu)
        menuItem = menu

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.edit_task -> {
                startActivity<EditTaskActivity>("task" to task)
                finish()
                true
            }
            R.id.delete_task -> {
                val builder = AlertDialog.Builder(this@DetailTaskActivity)
                builder.setTitle("Delete Activity")
                builder.setMessage("Do you really want to delete this activity?")
                builder.setPositiveButton("YES"){dialog, which ->
                    deleteData()
                    finish()
                }
                builder.setNegativeButton("No"){dialog,which ->
                    toast("Cancel")
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setData(){
        tv_date_task_detail.text = toDatetoString(task.taskDate.toString(), "EEEE, dd MMMM, HH:mm")
        tv_reps_detail.text = task.taskReps
        tv_sets_detail.text = task.taskSets
        tv_volume_detail.text = task.taskVolume
        tv_notes_detail.text = task.taskNote
        Glide.with(ctx).load(task.taskIcon).into(iv_icon_detail)
    }

    private fun deleteData(){
        val taskId = task.taskId.toString()
        val statusDelete = "0"
        APIClient.getService(this)
            .softDeleteTask(taskId, statusDelete)
            .enqueue(object : Callback<BasicResponse> {
                override fun onResponse(call: retrofit2.Call<BasicResponse>, response: Response<BasicResponse>) {
                    if (response.isSuccessful) {
                        database.use {
                            update("TABLE_TASK", "STATUS_DELETE" to "0")
                                .whereArgs("TASK_ID = {taskId}", "taskId" to task.taskId.toString())
                                .exec()
                        }
                        Toast.makeText(this@DetailTaskActivity, response.body()?.success, Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@DetailTaskActivity, "Failed", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                    Toast.makeText(this@DetailTaskActivity, "Error: $t", Toast.LENGTH_SHORT).show()
                }

            })
    }
}
