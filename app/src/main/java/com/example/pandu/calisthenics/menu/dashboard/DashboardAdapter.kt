package com.example.pandu.calisthenics.menu.dashboard

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.pandu.calisthenics.R
import com.example.pandu.calisthenics.model.TaskDay
import com.example.pandu.calisthenics.utils.getDateSplit
import com.example.pandu.calisthenics.utils.gone
import com.example.pandu.calisthenics.utils.toDatetoString
import kotlinx.android.synthetic.main.task_list.view.*

class DashboardAdapter(private val context: Context?, private val tasks : List<TaskDay>, private val listener : (TaskDay) -> Unit) : RecyclerView.Adapter<TaskViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TaskViewHolder {
        return TaskViewHolder(LayoutInflater.from(context).inflate(R.layout.task_list, p0, false))
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(p0: TaskViewHolder, p1: Int) {

        val curDate = getDateSplit(tasks[p1].taskDate.toString())
//        val type: String = tasks[position].activityId.toString()
//        Log.i("ACTIVITY_ID", type)
//        Log.i("CURRENT_DATE", curDate)
        var prevDate = ""
        if (p1 > 0){
            prevDate = getDateSplit(tasks[p1-1].taskDate.toString())
        }
        if (curDate == prevDate) {
            p0.itemView.tv_date_task.gone()
            p0.itemView.divider.gone()
        }

        p0.bindItem(tasks[p1],listener)
    }
}


class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view){
    fun bindItem(taskItem : TaskDay,  listener : (TaskDay) -> Unit){

        itemView.tv_sets_task.text = taskItem.taskSets
        itemView.tv_reps_task.text = taskItem.taskReps
        itemView.tv_name_task.text = taskItem.taskName
        itemView.tv_date_task.text = toDatetoString(taskItem.taskDate.toString(),"EEE, dd MMM")
        Glide.with(itemView.context).load(taskItem.taskIcon).into(itemView.iv_task)
        itemView.setOnClickListener(){
            listener(taskItem)
        }
    }

}