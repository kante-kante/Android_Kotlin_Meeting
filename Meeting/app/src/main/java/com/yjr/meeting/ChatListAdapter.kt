package com.yjr.meeting

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView

class ChatListAdapter(val context: Context, val chatList:ArrayList<ChatRoom>): RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        var chatRoomIv: CircleImageView = itemView.findViewById(R.id.chatroom_iv)
        var chatRoomTv: TextView = itemView.findViewById(R.id.chatroom_tv)
        var roomContentTv: TextView = itemView.findViewById(R.id.roomcontent_tv)

        fun bind(chatroom: ChatRoom) {

            chatRoomTv.text = chatroom.title
            roomContentTv.text = chatroom.content

            var firestore = FirebaseFirestore.getInstance()
            firestore.collection("chatroom").document(chatroom.uid).get()
                .addOnSuccessListener {
                    var room = it.toObject(ChatRoom::class.java)
                    chatRoomTv.text = room?.title
                    roomContentTv.text = room?.content
                }
            itemView.setOnClickListener{
                if(chatroom.title=="백엔드 인원 모집 방") {
                    var intent = Intent(context, ChatActivity::class.java)
                    context.startActivity(intent)
                }else{
                    var intent2 = Intent(context,ChatActivity2::class.java)
                    context.startActivity(intent2)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view=LayoutInflater.from(context).inflate(R.layout.item_chatlist,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(chatList[position])
    }

    override fun getItemCount(): Int {
        return chatList.size
    }


}