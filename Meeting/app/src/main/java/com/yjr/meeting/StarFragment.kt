package com.yjr.meeting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import kotlin.collections.ArrayList

class StarFragment: Fragment() {
    lateinit var profileIv: CircleImageView
    lateinit var nameTv: TextView
    lateinit var profileIntro: TextView

    //chat
    lateinit var roomList:ArrayList<ChatRoom>
    lateinit var chatList: RecyclerView
    lateinit var chatListAdapter: ChatListAdapter
    lateinit var instanace: StarFragment

    lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view=inflater.inflate(R.layout.fragment_star,container,false)
        chatList=view.findViewById(R.id.chatlist_rv)

        auth= FirebaseAuth.getInstance()
        firestore= FirebaseFirestore.getInstance()

        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //edit
        instanace=this
        roomList= ArrayList()

        profileIv = view.findViewById(R.id.profile_iv)
        nameTv = view.findViewById(R.id.nickname_tv)
        profileIntro = view.findViewById(R.id.profile_intro)

        //edit
        chatListAdapter= ChatListAdapter(activity!!,roomList)
        chatList.adapter=chatListAdapter
        chatList.layoutManager=LinearLayoutManager(activity)
        //
        firestore.collection("chatroom")
            .addSnapshotListener{ value, error ->
                if(value!=null){
                    for(dc in value.documentChanges){
                        if(dc.type==DocumentChange.Type.ADDED){
                            var firebaseChat = dc.document.toObject(ChatRoom::class.java)
                            firebaseChat.uid = dc.document.id
                            roomList.add(firebaseChat)
                            chatListAdapter.notifyDataSetChanged()
                            chatList.scrollToPosition(chatListAdapter.itemCount - 1)
                        }
                    }
                }

            }

        updateProfile()

    }

    fun updateProfile(){
        firestore.collection("User").document(auth.currentUser?.email!!).get()
            .addOnCompleteListener {
                if(it.isSuccessful){
                    //성공적으로 가져왔을 때
                    var user=it.result?.toObject(User::class.java)
                    //이렇게 가져온 정보를 텍스트뷰와 프로필에 넣어준다.
                    //여기서 글라이드가 필요하니 추가할 것
                    nameTv.text=user?.name
                    profileIntro.text=user?.profiletext

                    if(user?.profileUrl!=null) {
                        Glide.with(profileIv).load(user?.profileUrl).into(profileIv)
                    }
                }
            }
    }


}