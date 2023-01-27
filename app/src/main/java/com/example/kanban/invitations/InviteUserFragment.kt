package com.example.kanban.invitations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kanban.R
import com.example.kanban.currentUser
import com.example.kanban.database
import com.example.kanban.databinding.FragmentInviteUserBinding
import com.example.kanban.tables.Invitations
import com.example.kanban.tables.Projects
import com.example.kanban.tables.Users
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class InviteUserFragment : Fragment(), InviteClickListener {
    lateinit var inviteUserBinding: FragmentInviteUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        inviteUserBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_invite_user, container, false)
        return inviteUserBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inviteUserBinding.allUserListToInviteRecyclerView.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
        val adapter = InviteUserAdapter(this)
        inviteUserBinding.allUserListToInviteRecyclerView.adapter = adapter

        database.child("Users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val usersData = mutableListOf<Users>()
                snapshot.children.forEach { child ->
                    val eachUser = child.getValue(Users::class.java)
                    if (eachUser != null && eachUser.username != currentUser.username) {
                        usersData.add(eachUser)
                    }
                }
                adapter.submitList(usersData)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    companion object {
        @JvmStatic
        fun newInstance(): InviteUserFragment {
            return InviteUserFragment()
        }
    }

    override fun onBtnClick(data: Users) {
        val newInvitation = database.child("Invitations").push()

        val projectData = arguments?.getSerializable("project_data") as? Projects
        val invitation = Invitations(
            invitation_id = newInvitation.key,
            project_id = projectData?.project_id,
            invited_user = data.username,
            invited_by = currentUser.username,
            status = "pending"
        )
        newInvitation.setValue(invitation)
    }
}