package com.example.kanban.invitations

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kanban.R
import com.example.kanban.currentUser
import com.example.kanban.database
import com.example.kanban.databinding.ActivityInviteUserBinding
import com.example.kanban.tables.Invitations
import com.example.kanban.tables.Projects
import com.example.kanban.tables.Users
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class InviteUser : AppCompatActivity(), InviteClickListener {
    lateinit var inviteUserBinding: ActivityInviteUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inviteUserBinding = DataBindingUtil.setContentView(this@InviteUser,R.layout.activity_invite_user)

        inviteUserBinding.allUserListToInviteRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
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

    override fun onBtnClick(data: Users) {
        val newInvitation = database.child("Invitations").push()
        val projectData = intent.getSerializableExtra("projectData") as? Projects
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