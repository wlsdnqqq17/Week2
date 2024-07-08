package com.example.week2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FriendRequestAdapter(private val friendRequestList: List<User>, private val onAcceptClick: (User) -> Unit) : RecyclerView.Adapter<FriendRequestAdapter.FriendRequestViewHolder>() {

    class FriendRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val friendId: TextView = itemView.findViewById(R.id.friend_add_item_id)
        val friendName: TextView = itemView.findViewById(R.id.friend_add_item_name)
        val acceptButton: Button = itemView.findViewById(R.id.friend_add_Button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendRequestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.friend_add_recycler_view_item, parent, false)
        return FriendRequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendRequestViewHolder, position: Int) {
        val friend = friendRequestList[position]
        holder.friendId.text = friend.login_id
        holder.friendName.text = friend.nickname
        holder.acceptButton.setOnClickListener { onAcceptClick(friend) }
    }

    override fun getItemCount(): Int {
        return friendRequestList.size
    }
}
