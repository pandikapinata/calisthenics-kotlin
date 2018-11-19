package com.example.pandu.calisthenics.menu.task

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.pandu.calisthenics.R
import com.example.pandu.calisthenics.gone
import com.example.pandu.calisthenics.invisible
import com.example.pandu.calisthenics.model.ActivityItem
import com.example.pandu.calisthenics.model.Task
import com.example.pandu.calisthenics.toDatetoString
import kotlinx.android.synthetic.main.task_list.view.*

class TaskAdapter( private val tasks : List<Task>, private val listener : (Task) -> Unit) : RecyclerView.Adapter<TaskViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.task_list, parent, false))
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
//        val activityItem : ActivityItem
//        var nameActivity = tasks[position].activityId
        val curDate = tasks[position].taskDate
        var prevDate = ""
        if (position > 0){
            prevDate = tasks[position-1].taskDate.toString()
        }
        if (curDate.equals(prevDate)) {
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
        Glide.with(itemView.context).load(taskItem.taskIcon).into(itemView.iv_task)
        itemView.setOnClickListener(){
            listener(taskItem)
        }
    }

}
