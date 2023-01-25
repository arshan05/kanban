package com.example.kanban.homescreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kanban.R
import com.example.kanban.currentUser
import com.example.kanban.database
import com.example.kanban.databinding.ActivityHomescreenBinding
import com.example.kanban.databinding.NavigationDrawerBinding
import com.example.kanban.invitations.AllInvitations
import com.example.kanban.project.ProjectDetailed
import com.example.kanban.tables.Projects
import com.example.kanban.tables.Users
import com.example.kanban.tables.Users_Projects
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import kotlin.reflect.typeOf

class Homescreen : AppCompatActivity(), ProjectCardViewClickListener {
    lateinit var homescreenBinding: ActivityHomescreenBinding
    lateinit var navigationDrawerBinding: NavigationDrawerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homescreenBinding =
            DataBindingUtil.setContentView(this@Homescreen, R.layout.activity_homescreen)
        navigationDrawerBinding = DataBindingUtil.setContentView(this@Homescreen, R.layout.navigation_drawer)

//        setSupportActionBar(navigationDrawerBinding.topAppBar)
//        navigationDrawerBinding.topAppBar.setNavigationOnClickListener {
//            navigationDrawerBinding.drawerLayout.open()
//        }

//        navigationDrawerBinding.navigationView.setNavigationItemSelectedListener {
//            when(it.itemId){
//                R.id.homeItem -> {
//                    val intent = Intent(this@Homescreen, Homescreen::class.java)
//                    startActivity(intent)
//                    true
//                }
//                R.id.invitationItem -> {
//                    val intent = Intent(this@Homescreen, AllInvitations::class.java)
//                    startActivity(intent)
//                    true
//                }
//                else ->
//                    false
//            }
//
//            navigationDrawerBinding.drawerLayout.close()
//            true
//        }

        homescreenBinding.user = currentUser

//        homescreenBinding.invitationsReceivedOrSent.setOnClickListener{
//            val intent = Intent(this@Homescreen, AllInvitations::class.java)
//            startActivity(intent)
//        }


        homescreenBinding.addProjectBtn.setOnClickListener {
            val newProject = database.child("Projects").push()
            val project = Projects(
                project_id = newProject.key,
                project_name = homescreenBinding.projectNameInput.editText?.text.toString(),
                owner = currentUser.username.toString()
            )
            newProject.setValue(project)

            val userProject = database.child("Users_Projects").push()
            val usersProjects = Users_Projects(
                user_id = currentUser.username,
                project_id = newProject.key
            )
            userProject.setValue(usersProjects)
        }

        homescreenBinding.projectDisplayRecyclerView.layoutManager =
            LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        val adapter = HomeScreenAdapter(this)
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

    }

    override fun onItemClicked(data: Projects) {
        val intent = Intent(this,ProjectDetailed::class.java)
        intent.putExtra("projectData", data )
        startActivity(intent)
    }
}