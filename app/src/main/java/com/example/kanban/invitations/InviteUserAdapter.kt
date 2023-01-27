package com.example.kanban.invitations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kanban.currentUser
import com.example.kanban.database
import com.example.kanban.databinding.ActivityProjectItemBinding
import com.example.kanban.databinding.UserListItemBinding
import com.example.kanban.homescreen.ProjectCardViewClickListener
import com.example.kanban.tables.Invitations
import com.example.kanban.tables.Projects
import com.example.kanban.tables.Users


class InviteUserAdapter(val inviteClickListener: InviteClickListener): ListAdapter<Users, UsersListViewHolder>(DiffCallback) {
    lateinit var userListItemBinding: UserListItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersListViewHolder {
        userListItemBinding = UserListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UsersListViewHolder(userListItemBinding)
    }

    override fun onBindViewHolder(holder: UsersListViewHolder, position: Int) {
        holder.usersItemBinding.user = getItem(position)
        holder.usersItemBinding.inviteUserBtn.setOnClickListener{
            inviteClickListener.onBtnClick(getItem(position))
        }
    }
}

interface InviteClickListener {
    fun onBtnClick(data:Users)
}

val DiffCallback = object : DiffUtil.ItemCallback<Users>(){
    override fun areItemsTheSame(oldItem: Users, newItem: Users): Boolean {
        return oldItem.username == newItem.username
    }

    override fun areContentsTheSame(oldItem: Users, newItem: Users): Boolean {
        return oldItem == newItem
    }

}

class UsersListViewHolder(var usersItemBinding: UserListItemBinding): RecyclerView.ViewHolder(usersItemBinding.root) {
}
