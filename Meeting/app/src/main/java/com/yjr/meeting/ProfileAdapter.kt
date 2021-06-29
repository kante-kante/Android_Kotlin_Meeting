package com.yjr.meeting

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ProfileAdapter(var context: Context,var postList:ArrayList<Post>):RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {
    // 프로필 하단에 나오는 그림들을 구현하기 위함.
    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        var imageIv: ImageView =itemView.findViewById(R.id.image_iv)
        fun bind(post:Post){
            Glide.with(imageIv).load(post.imageUrl).into(imageIv)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileAdapter.ViewHolder {
        var view=LayoutInflater.from(context).inflate(R.layout.item_profile,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int { //리스트를 얼마나 가져올지
        return postList.size
    }

    override fun onBindViewHolder(holder: ProfileAdapter.ViewHolder, position: Int) { //각 한개의 아이템이 어떻게 정의될지를..
        var post=postList[position]
        holder.bind(post)
    }
}