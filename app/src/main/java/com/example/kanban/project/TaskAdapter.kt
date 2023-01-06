package com.example.kanban.project

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kanban.databinding.ActivityProjectItemBinding
import com.example.kanban.databinding.TasksItemBinding
import com.example.kanban.homescreen.ProjectsViewHolder
import com.example.kanban.tables.Projects
import com.example.kanban.tables.Tasks

class TaskAdapter:ListAdapter<Tasks,TasksViewHolder>(DiffCallback) {
    lateinit var tasksItemBinding: TasksItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
         tasksItemBinding = TasksItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TasksViewHolder(tasksItemBinding)
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        holder.tasksItemBinding.task = getItem(position)
    }
}

val DiffCallback = object : DiffUtil.ItemCallback<Tasks>(){
    override fun areItemsTheSame(oldItem: Tasks, newItem: Tasks): Boolean {
        return oldItem.task_id == newItem.task_id
    }

    override fun areContentsTheSame(oldItem: Tasks, newItem: Tasks): Boolean {
        return oldItem == newItem
    }

}

class TasksViewHolder(var tasksItemBinding: TasksItemBinding): RecyclerView.ViewHolder(tasksItemBinding.root) {
}
