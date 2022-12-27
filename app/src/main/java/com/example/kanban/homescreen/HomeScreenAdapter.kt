package com.example.kanban.homescreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kanban.databinding.ActivityProjectItemBinding
import com.example.kanban.tables.Projects

class HomeScreenAdapter : ListAdapter<Projects, ProjectsViewHolder>(DiffCallback) {
    lateinit var projectItemBinding: ActivityProjectItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectsViewHolder {
        projectItemBinding = ActivityProjectItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProjectsViewHolder(projectItemBinding)
    }

    override fun onBindViewHolder(holder: ProjectsViewHolder, position: Int) {
        holder.projectItemBinding.project = getItem(position)
    }
}

val DiffCallback = object : DiffUtil.ItemCallback<Projects>(){
    override fun areItemsTheSame(oldItem: Projects, newItem: Projects): Boolean {
        return oldItem.project_id == newItem.project_id
    }

    override fun areContentsTheSame(oldItem: Projects, newItem: Projects): Boolean {
        return oldItem == newItem
    }

}

class ProjectsViewHolder(var projectItemBinding: ActivityProjectItemBinding):RecyclerView.ViewHolder(projectItemBinding.root) {
}
