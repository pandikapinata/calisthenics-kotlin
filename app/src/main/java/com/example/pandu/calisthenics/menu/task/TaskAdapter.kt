package com.example.pandu.calisthenics.menu.task

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.pandu.calisthenics.*
import com.example.pandu.calisthenics.model.Task
import com.example.pandu.calisthenics.utils.getDateSplit
import com.example.pandu.calisthenics.utils.gone
import com.example.pandu.calisthenics.utils.toDatetoString
import kotlinx.android.synthetic.main.task_list.view.*

class TaskAdapter(private val context: Context?, private val tasks : List<Task>, private val listener : (Task) -> Unit) : RecyclerView.Adapter<TaskViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(LayoutInflater.from(context).inflate(R.layout.task_list, parent, false))
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {

        val curDate = getDateSplit(tasks[position].taskDate.toString())
//        val type: String = tasks[position].activityId.toString()
//        Log.i("ACTIVITY_ID", type)
//        Log.i("CURRENT_DATE", curDate)
        var prevDate = ""
        if (position > 0){
            prevDate = getDateSplit(tasks[position-1].taskDate.toString())
        }
        if (curDate == prevDate) {
            holder.itemView.tv_date_task.gone()
            holder.itemView.divider.gone()
        }

        holder.bindItem(tasks[position],listener)
    }
}

class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view){
    fun bindItem(taskItem : Task,  listener : (Task) -> Unit){

        itemView.tv_sets_task.text = taskItem.taskSets
        itemView.tv_reps_task.text = taskItem.taskReps
        itemView.tv_name_task.text = taskItem.taskName
        itemView.tv_date_task.text = toDatetoString(taskItem.taskDate.toString(),"EEE, dd MMM")
        itemView.tv_time_task.text = toDatetoString(taskItem.taskDate.toString(),"HH:mm")
        Glide.with(itemView.context).load(taskItem.taskIcon).into(itemView.iv_task)
        itemView.setOnClickListener(){
            listener(taskItem)
        }
    }

}
