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

class HomeAdapter(var context:Context,var postList:ArrayList<Post>):RecyclerView.Adapter<HomeAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var profileIv:ImageView=itemView.findViewById(R.id.profile_iv)
        var nameTv:TextView= itemView.findViewById(R.id.name_tv)
        var imageIv:ImageView=itemView.findViewById(R.id.image_iv)
        var textTv:TextView=itemView.findViewById(R.id.text_tv)
        var projectName:TextView=itemView.findViewById(R.id.project_name)
        var projectNum:TextView=itemView.findViewById(R.id.project_num)


        fun bind(post:Post){

            textTv.text=post.text
            projectName.text=post.projectname
            projectNum.text=post.projectnum

            Glide.with(imageIv).load(post.imageUrl).into(imageIv) //이미지뷰에 보내준다.
            var firestore=FirebaseFirestore.getInstance()
            firestore.collection("User").document(post.userId).get()
                    .addOnSuccessListener {
                        var user=it.toObject(User::class.java)
                        Glide.with(profileIv).load(user?.profileUrl).into(profileIv)
                        nameTv.text=user?.name

                        //레이아웃에 배치하도록 만들어주는 소스코드였다.
                    }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapter.ViewHolder {
        //레이아웃을 가져오는 과정
        var view = LayoutInflater.from(context).inflate(R.layout.item_home,parent,false)
        //가져온 레이아웃을 뷰홀더에 리턴
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return postList.size
        //몇개의 레이아웃을 보내냐..
    }

    override fun onBindViewHolder(holder: HomeAdapter.ViewHolder, position: Int) {
        //바인드로 호출해주면 된다.
        var post=postList[position]
        holder.bind(post)
    }


}