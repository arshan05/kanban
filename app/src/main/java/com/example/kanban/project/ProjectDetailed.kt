package com.example.kanban.project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kanban.R
import com.example.kanban.currentUser
import com.example.kanban.database
import com.example.kanban.databinding.ActivityProjectDetailedBinding
import com.example.kanban.invitations.InviteUser
import com.example.kanban.tables.Projects
import com.example.kanban.tables.Tasks
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.Objects

class ProjectDetailed : AppCompatActivity() {
    lateinit var projectDetailedBinding: ActivityProjectDetailedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        projectDetailedBinding = DataBindingUtil.setContentView(this,R.layout.activity_project_detailed)

        val data = intent.getSerializableExtra("projectData") as? Projects
        projectDetailedBinding.projectData = data

        projectDetailedBinding.inviteBtn.setOnClickListener{
            val intent = Intent(this, InviteUser::class.java)
            intent.putExtra("projectData", data)
            startActivity(intent)
        }

        projectDetailedBinding.addTaskBtn.setOnClickListener{
            val newTask = database.child("Tasks").push()
            val task = Tasks(
                task_id = newTask.key,
                task_name = projectDetailedBinding.taskNameInput.editText?.text.toString(),
                project_id = data?.project_id,
                status = null,
                assigned_id = null
            )
            newTask.setValue(task)

        }

        projectDetailedBinding.tasksRecyclerView.layoutManager = LinearLayoutManager(applicationContext,RecyclerView.VERTICAL, false)
        val adapter = TaskAdapter()
        projectDetailedBinding.tasksRecyclerView.adapter = adapter

        database.child("Tasks").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tasksData = mutableListOf<Tasks>()
                snapshot.children.forEach { child ->
                    val eachProject = child.getValue(Tasks::class.java)
//                        Log.d("project", "${eachProject?.project_id} ${eachProject?.project_name} ${eachProject?.owner}")
//                            Log.d("project", "${child.value} ")
//                        Log.d("sep","____")
                    if (eachProject != null && eachProject.project_id == data?.project_id) {
                        tasksData.add(Tasks(
                            task_id = eachProject.task_id,
                            task_name = eachProject.task_name,
                            project_id = eachProject.project_id,
                            status = eachProject.status,
                            assigned_id = eachProject.assigned_id
                        ))
                    }
                }
                adapter.submitList(tasksData)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })


    }
}