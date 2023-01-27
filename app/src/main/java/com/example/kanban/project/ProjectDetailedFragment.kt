package com.example.kanban.project

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kanban.R
import com.example.kanban.database
import com.example.kanban.databinding.FragmentProjectDetailedBinding
import com.example.kanban.invitations.InviteUserFragment
import com.example.kanban.tables.Projects
import com.example.kanban.tables.Tasks
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ProjectDetailedFragment : Fragment() {

    lateinit var projectDetailedBinding: FragmentProjectDetailedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        projectDetailedBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_project_detailed, container, false)
        return projectDetailedBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = arguments?.getSerializable("project_data") as? Projects
        projectDetailedBinding.projectData = data

        projectDetailedBinding.inviteBtn.setOnClickListener{

            val dataBundle = Bundle()
            dataBundle.putSerializable("project_data",data)
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val destination = InviteUserFragment()
            destination.arguments = dataBundle

            fragmentTransaction.replace(R.id.nav_host_fragment, destination)
            fragmentTransaction.addToBackStack(null)
            fragmentManager.popBackStack()
            fragmentTransaction.commit()

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

        projectDetailedBinding.tasksRecyclerView.layoutManager = LinearLayoutManager(view.context,
            RecyclerView.VERTICAL, false)
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
                        tasksData.add(
                            Tasks(
                            task_id = eachProject.task_id,
                            task_name = eachProject.task_name,
                            project_id = eachProject.project_id,
                            status = eachProject.status,
                            assigned_id = eachProject.assigned_id
                        )
                        )
                    }
                }
                adapter.submitList(tasksData)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }

    companion object {

        @JvmStatic
        fun newInstance(): ProjectDetailedFragment {
            return ProjectDetailedFragment()
        }
    }
}
