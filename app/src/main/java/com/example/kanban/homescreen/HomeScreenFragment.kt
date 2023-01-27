package com.example.kanban.homescreen

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kanban.R
import com.example.kanban.currentUser
import com.example.kanban.database
import com.example.kanban.databinding.FragmentHomeScreenBinding
import com.example.kanban.project.ProjectDetailedFragment
import com.example.kanban.tables.Projects
import com.example.kanban.tables.Users_Projects
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class HomeScreenFragment : Fragment(),ProjectCardViewClickListener {

    lateinit var homeScreenBinding: FragmentHomeScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeScreenBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home_screen, container, false)
        return homeScreenBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeScreenBinding.user = currentUser


        homeScreenBinding.addProjectBtn.setOnClickListener {
            val newProject = database.child("Projects").push()
            val project = Projects(
                project_id = newProject.key,
                project_name = homeScreenBinding.projectNameInput.editText?.text.toString(),
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

        homeScreenBinding.projectDisplayRecyclerView.layoutManager =
            LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
        val adapter = HomeScreenAdapter(this)
        homeScreenBinding.projectDisplayRecyclerView.adapter = adapter

        database.child("Projects").addValueEventListener(object : ValueEventListener {
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
        val dataBundle = Bundle()
        dataBundle.putSerializable("project_data",data)
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val destination = ProjectDetailedFragment()
        destination.arguments = dataBundle

        fragmentTransaction.replace(R.id.nav_host_fragment, destination)
        fragmentTransaction.addToBackStack(null)
        fragmentManager.popBackStack()
        fragmentTransaction.commit()

//        val intent = Intent(view?.context, ProjectDetailed::class.java)
//        intent.putExtra("projectData", data )
//        startActivity(intent)
    }

    companion object {
        @JvmStatic
        fun newInstance(): HomeScreenFragment {
            return HomeScreenFragment()
        }
    }
}