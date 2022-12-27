package com.example.kanban.homescreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kanban.R
import com.example.kanban.currentUser
import com.example.kanban.database
import com.example.kanban.databinding.ActivityHomescreenBinding
import com.example.kanban.tables.Projects
import com.example.kanban.tables.Users
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import kotlin.reflect.typeOf

class Homescreen : AppCompatActivity() {
    lateinit var homescreenBinding: ActivityHomescreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homescreenBinding =
            DataBindingUtil.setContentView(this@Homescreen, R.layout.activity_homescreen)



        currentUser = Users("Darshan", "darshan", "pass123")
        homescreenBinding.user = currentUser


        homescreenBinding.addProjectBtn.setOnClickListener {
            val newProject = database.child("Projects").push()
            val project = Projects(
                project_id = newProject.key,
                project_name = homescreenBinding.projectNameInput.editText?.text.toString(),
                owner = currentUser.username.toString()
            )
            newProject.setValue(project)
        }

        homescreenBinding.projectDisplayRecyclerView.layoutManager =
            LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        val adapter = HomeScreenAdapter()
        homescreenBinding.projectDisplayRecyclerView.adapter = adapter

        database.child("Projects").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val projectsData = mutableListOf<Projects>()
                snapshot.children.forEach { child ->
                        val eachProject = child.getValue(Projects::class.java)
//                        Log.d("project", "${eachProject?.project_id} ${eachProject?.project_name} ${eachProject?.owner}")
//                            Log.d("project", "${child.value} ")
//                        Log.d("sep","____")
                        if (eachProject != null) {
                            projectsData.add(Projects(project_id = eachProject.project_id, project_name = eachProject.project_name, owner =  eachProject.owner))
                        }
                    }
                    adapter.submitList(projectsData)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

//        lifecycle.coroutineScope.launch {
//            database.child("Projects").addChildEventListener(object : ChildEventListener {
//                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//                    snapshot.children.forEach { child ->
//                        val eachProject = child.value
////                        Log.d("project", "${eachProject?.project_id} ${eachProject?.project_name} ${eachProject?.owner}")
//                            Log.d("project", "${child.value} ")
//                        Log.d("sep","____")
////                        if (project != null) {
////                            projectsData.add(Projects(project_id = project.project_id, project_name = project.project_name, owner =  project.owner))
////                        }
//                    }
//                    adapter.submitList(projectsData)
//                }
//
//                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
//
//                }
//
//                override fun onChildRemoved(snapshot: DataSnapshot) {
//                }
//
//                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
//
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    Log.w("warning", "postComments:onCancelled", error.toException())
//                    Toast.makeText(applicationContext, "Failed to load comments.",
//                        Toast.LENGTH_SHORT).show()
//                }
//
//
//            })
//        }


//            .get().addOnSuccessListener {
//            val data = it.getValue(Projects::class.java)
//            Log.d("data" ,"Got value ${data?.project_id} ${data?.project_name} ${data?.owner}")
//        }

    }
}