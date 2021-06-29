package com.yjr.meeting

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList

class StarAdapter(var context:Context, var messageList:ArrayList<Message>): RecyclerView.Adapter<StarAdapter.ViewHolder> (){

    var itemClickListener:ItemClickListener?=null // null 삽입 시 ? 삽입

    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        var profileIv: ImageView =itemView.findViewById(R.id.profile_iv)
        var nicknameTv:TextView=itemView.findViewById(R.id.nickname_tv)
        var messageTv:TextView=itemView.findViewById(R.id.message_tv)
        var timeTv:TextView=itemView.findViewById(R.id.time_tv)


        fun bind(message:Message){

            messageTv.text=message.message
            timeTv.text=String.format("%02d:%02d",message.date.hours,message.date.minutes) //message,time 값을 messageTv,timeTv에 보여주는 역할을 한다

            var firestore=FirebaseFirestore.getInstance()
            firestore.collection("User").document(message.email).get()
                .addOnSuccessListener {
                    var user=it.toObject(User::class.java)
                    Glide.with(profileIv).load(user?.profileUrl).into(profileIv)
                    nicknameTv.text=user?.name
                }

            itemView.setOnLongClickListener{
                if(itemClickListener!=null){
                    itemClickListener?.onLongClick(message)
                }
                true
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view=LayoutInflater.from(context).inflate(R.layout.item_message,parent,false)//oncreate 부모에 붙이지 않겠다.
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {// 몇번째 아이템을 그릴것인지
        holder.bind(messageList[position])
    }

    interface ItemClickListener{ // 인터페이스 생성 시에는 괄호를 열고 닫아주지 않아도 됨
        fun onLongClick(message:Message)
    }
}