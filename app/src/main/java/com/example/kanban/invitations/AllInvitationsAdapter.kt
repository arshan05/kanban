package com.example.kanban.invitations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kanban.databinding.AllInvitationsItemBinding
import com.example.kanban.tables.Invitations


class AllInvitationsAdapter(val invitationClickListener: InvitationClickListener): ListAdapter<Invitations, AllInvitationsViewHolder>(InvitationDiffCallback) {
    lateinit var allInvitationsItemBinding: AllInvitationsItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllInvitationsViewHolder {
        allInvitationsItemBinding = AllInvitationsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllInvitationsViewHolder(allInvitationsItemBinding)
    }

    override fun onBindViewHolder(holder: AllInvitationsViewHolder, position: Int) {
        holder.allInvitationsItemBinding.invitation = getItem(position)
        holder.allInvitationsItemBinding.invitationAcceptButton.setOnClickListener{
            invitationClickListener.acceptBtn(getItem(position),position)
        }
        holder.allInvitationsItemBinding.invitationRejectButton.setOnClickListener{
            invitationClickListener.rejectBtn(getItem(position))
        }
    }
}

interface InvitationClickListener {
    fun acceptBtn(data: Invitations, position: Int)
    fun rejectBtn(data: Invitations)
}

val InvitationDiffCallback = object : DiffUtil.ItemCallback<Invitations>(){
    override fun areItemsTheSame(oldItem: Invitations, newItem: Invitations): Boolean {
        return oldItem.invitation_id == newItem.invitation_id
    }

    override fun areContentsTheSame(oldItem: Invitations, newItem: Invitations): Boolean {
        return oldItem == newItem
    }

}

class AllInvitationsViewHolder(var allInvitationsItemBinding: AllInvitationsItemBinding): RecyclerView.ViewHolder(allInvitationsItemBinding.root) {
}
