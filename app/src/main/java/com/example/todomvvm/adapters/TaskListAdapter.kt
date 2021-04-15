package com.example.todomvvm.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todomvvm.R
import com.example.todomvvm.data.Task

class TaskListAdapter(taskItemClickListener: TaskItemClickListener) :
    ListAdapter<Task, TaskListAdapter.TaskViewHolder>(
        TasksComparator()
    ) {

    private val listener: TaskItemClickListener = taskItemClickListener
    private var taskList: List<Task> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, listener)
        holder.itemView.apply {

        }
    }

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(task: Task, listener: TaskItemClickListener) {

            val taskItemViewTitle: TextView = itemView.findViewById(R.id.textView_title)
            val taskItemViewDesc: TextView = itemView.findViewById(R.id.textView_desc)
            val taskItemViewDate: TextView = itemView.findViewById(R.id.TextView_date)
            val checkBox: CheckBox = itemView.findViewById(R.id.checkbox)

            taskItemViewTitle.text = task.task
            taskItemViewDesc.text = task.desc
            taskItemViewDate.text = task.created
            checkBox.isChecked = task.completed

            if(task.completed) {
                taskItemViewTitle.apply {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
                taskItemViewDesc.apply {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
                taskItemViewDate.apply {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
            } else {
                taskItemViewTitle.apply {
                    paintFlags = 0
                }
                taskItemViewDesc.apply {
                    paintFlags = 0
                }
                taskItemViewDate.apply {
                    paintFlags = 0
                }

            }

            itemView.setOnLongClickListener {
                listener.onLongClick(task)
                true
            }

            itemView.setOnClickListener {
                listener.onClick(task)
            }

            checkBox.setOnClickListener {
                listener.onClick(task)
            }
        }

        companion object {
            fun create(parent: ViewGroup): TaskViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)
                return TaskViewHolder(view)
            }
        }
    }

    class TasksComparator : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id === newItem.id
        }

        //if any item changes in task, this method will update it
        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.task == newItem.task
        }
    }

    interface TaskItemClickListener {
        fun onLongClick(task: Task)
        fun onClick(task: Task)
        fun onCheckClicked(task: Task)
    }

    fun setTasks(task: List<Task>){
        this.taskList = task
        notifyDataSetChanged()

    }
}