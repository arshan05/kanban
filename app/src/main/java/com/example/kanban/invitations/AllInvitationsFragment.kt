package com.example.kanban.invitations

import android.os.Bundle
import android.util.Log
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
import com.example.kanban.databinding.FragmentAllInvitationsBinding
import com.example.kanban.tables.Invitations
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

class AllInvitationsFragment : Fragment(), InvitationClickListener {

    lateinit var allInvitationsBinding: FragmentAllInvitationsBinding
    var pos = -1
    var i = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        allInvitationsBinding =  DataBindingUtil.inflate(inflater,R.layout.fragment_all_invitations,container,false)
        return allInvitationsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        allInvitationsBinding.allInvitationsReceivedRecyclerView.layoutManager =
            LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
        val receivedAdapter = AllInvitationsAdapter(this)
        allInvitationsBinding.allInvitationsReceivedRecyclerView.adapter = receivedAdapter

        allInvitationsBinding.allInvitationsRequestedRecyclerView.layoutManager =
            LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
        val requestedAdapter = AllInvitationsAdapter(this)
        allInvitationsBinding.allInvitationsRequestedRecyclerView.adapter = requestedAdapter

        database.child("Invitations").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//                val invitationReceivedData = snapshot.getValue(Invitations::class.java)
                val invitationData = snapshot.getValue(Invitations::class.java)
                i++
                Log.d(
                    "data",
                    "from ${invitationData?.invited_by} to ${invitationData?.invited_user} $i"
                )

                if (invitationData?.invited_by == currentUser.username) {
                    requestedAdapter.submitList(requestedAdapter.currentList.toMutableList().apply {
                        add(invitationData)
                    })
                }

                if (invitationData?.invited_user == currentUser.username) {
                    receivedAdapter.submitList(receivedAdapter.currentList.toMutableList().apply {
                        add(invitationData)
                    })
                }

//                snapshot.children.forEach {
//                    val invitationData = it.getValue(Invitations::class.java)
//                    receivedAdapter.submitList(receivedAdapter.currentList.toMutableList().apply { if (invitationData?.invited_user == currentUser.username) add(invitationData) })
//                    requestedAdapter.submitList(requestedAdapter.currentList.toMutableList().apply { if (invitationData?.invited_by == currentUser.username) add(invitationData) })
//                }


//                receivedAdapter.submitList(receivedAdapter.currentList.toMutableList().apply { if (invitationData?.invited_user == currentUser.username) add(invitationData) })
//                requestedAdapter.submitList(requestedAdapter.currentList.toMutableList().apply { if (invitationData?.invited_by == currentUser.username) add(invitationData) })

//                val receivedInvitationData = mutableListOf<Invitations>()
//                val requestedInvitationData = mutableListOf<Invitations>()
//                snapshot.children.forEach { child ->
//                    val eachInvitations = child.getValue(Invitations::class.java)
//                    if (eachInvitations != null && eachInvitations.invited_user == currentUser.username && eachInvitations.status == "pending") {
//                        receivedInvitationData.add(eachInvitations)
//                    }
//                    if (eachInvitations != null && eachInvitations.invited_by == currentUser.username) {
//                        requestedInvitationData.add(eachInvitations)
//                    }
//                }
//                receivedAdapter.submitList(receivedInvitationData)
//                requestedAdapter.submitList(requestedInvitationData)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val invitationUpdated = snapshot.getValue(Invitations::class.java)
                if (invitationUpdated?.invited_by == currentUser.username) {
                    val index = requestedAdapter.currentList.indexOf(invitationUpdated)
                    requestedAdapter.notifyItemChanged(index)
                }


                if (invitationUpdated?.invited_user == currentUser.username) {
                    val index = requestedAdapter.currentList.indexOf(invitationUpdated)
                    receivedAdapter.notifyItemChanged(index)
                }


//                val invitation = snapshot.getValue(Invitations::class.java)
//                val indexOfRequest = requestedAdapter.currentList.indexOf(invitation)
//                val indexOfReceived = receivedAdapter.currentList.indexOf(invitation)
//                i++
////                Toast.makeText(applicationContext, "data changed index $indexOfRequest, position $pos", Toast.LENGTH_SHORT).show()
//                Log.d("changed","ind $indexOfRequest i $i")
//                if (indexOfReceived != -1) {
//                    receivedAdapter.submitList(receivedAdapter.currentList.toMutableList().apply { set(indexOfReceived, invitation) })
////                    receivedAdapter.notifyItemChanged(indexOfReceived)
//                }
//                if(indexOfRequest != -1){
//                    requestedAdapter.submitList(requestedAdapter.currentList.toMutableList().apply {
//                        set(indexOfRequest,invitation)
//                    }

            }


            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

//        database.child("Invitations").addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val receivedInvitationData = mutableListOf<Invitations>()
//                val requestedInvitationData = mutableListOf<Invitations>()
//                snapshot.children.forEach { child ->
//                    val eachInvitations = child.getValue(Invitations::class.java)
//                    if (eachInvitations != null && eachInvitations.invited_user == currentUser.username && eachInvitations.status == "pending") {
//                        receivedInvitationData.add(eachInvitations)
//                    }
//                    if (eachInvitations != null && eachInvitations.invited_by == currentUser.username) {
//                        requestedInvitationData.add(eachInvitations)
//                    }
//                }
//                receivedAdapter.submitList(receivedInvitationData)
//                requestedAdapter.submitList(requestedInvitationData)
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//            }
//
//        })

    }

    companion object {

        @JvmStatic
        fun newInstance(): AllInvitationsFragment {
            return AllInvitationsFragment()
        }
    }

    override fun acceptBtn(data: Invitations, position: Int) {
        database.child("Invitations").child(data.invitation_id.toString()).child("status")
            .setValue("accepted")
        pos = position
//        val userProject = database.child("Users_Projects").push()
//        val usersProjects = Users_Projects(
//            user_id = currentUser.username,
//            project_id = data.project_id
//        )
//        userProject.setValue(usersProjects)
    }

    override fun rejectBtn(data: Invitations) {
        database.child("Invitations").child(data.invitation_id.toString()).child("status")
            .setValue("rejected")
    }
}