package com.example.pandu.calisthenics.menu.detailTask

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.example.pandu.calisthenics.R
import com.example.pandu.calisthenics.model.Task
import com.example.pandu.calisthenics.toDatetoString
import kotlinx.android.synthetic.main.activity_detail_task.*
import org.jetbrains.anko.ctx

class DetailTaskActivity : AppCompatActivity() {

    private var toolbarAct: Toolbar? = null
    private lateinit var task : Task
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setData(){
        tv_date_task_detail.text = toDatetoString(task.taskDate.toString(), "EEEE, dd MMMM")
        tv_reps_detail.text = task.taskReps
        tv_sets_detail.text = task.taskSets
        tv_volume_detail.text = task.taskVolume
        Glide.with(ctx).load(task.taskIcon).into(iv_icon_detail)
    }
}
