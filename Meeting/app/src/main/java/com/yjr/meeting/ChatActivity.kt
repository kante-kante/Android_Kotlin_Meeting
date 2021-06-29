package com.yjr.meeting

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

class ChatActivity: AppCompatActivity() {
    lateinit var profileIv: CircleImageView
    lateinit var nameTv: TextView
    lateinit var profileIntro: TextView

    //chat
    lateinit var chattingRv: RecyclerView
    lateinit var submitBtn2: Button
    lateinit var messageEt: EditText
    lateinit var messageAdapter:StarAdapter
    lateinit var messageList:ArrayList<Message>


    lateinit var auth: FirebaseAuth
    lateinit var firestore:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        profileIv = findViewById(R.id.profile_iv)
        nameTv = findViewById(R.id.nickname_tv)
        profileIntro = findViewById(R.id.profile_intro)

        chattingRv=findViewById(R.id.list_rv)
        submitBtn2=findViewById(R.id.submit2_btn)
        messageEt=findViewById(R.id.message_et)

        messageList=ArrayList()
        messageAdapter = StarAdapter(this,messageList)
        chattingRv.adapter=messageAdapter
        chattingRv.layoutManager=LinearLayoutManager(this)

        auth= FirebaseAuth.getInstance()
        firestore= FirebaseFirestore.getInstance()

        firestore?.collection("message")
            .orderBy("date", Query.Direction.ASCENDING) // date필드를 기준으로 오름차순. 맨 아래 채팅이 최신
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (querySnapshot != null) {

                    for(dc in querySnapshot.documentChanges) {
                        //Firebase에 추가된 메세지를 messageList에 추가
                        if (dc.type == DocumentChange.Type.ADDED) {
                            var firebaseMessage = dc.document.toObject(Message::class.java)
                            firebaseMessage.id = dc.document.id
                            messageList.add(firebaseMessage)
                            messageAdapter.notifyDataSetChanged()
                            chattingRv.scrollToPosition(messageAdapter.itemCount - 1)
                        }
                        //Firebase에 삭제된 메세지를 messageList에서도 삭제
                        if (dc.type == DocumentChange.Type.REMOVED) {
                            var findedMessage =
                                messageList.filter { message -> message.id == dc.document.id }
                            messageList.remove(findedMessage[0])
                            messageAdapter.notifyDataSetChanged()
                        }
                        //Firebase에 수정된 메세지를 messageList에서도 수정
                        if (dc.type == DocumentChange.Type.MODIFIED) {
                            var firebaseMessage = dc.document.toObject(Message::class.java)
                            var findedMessage =
                                messageList.filter { message -> message.id == dc.document.id }
                            var messageIndex = messageList.indexOf(findedMessage[0])
                            messageList.get(messageIndex).message = firebaseMessage.message
                            messageAdapter.notifyDataSetChanged()
                        }

                    }
                }
            }


        submitBtn2.setOnClickListener {
            onClickSubmitBtn()
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

    fun onClickSubmitBtn(){
        var msg=messageEt.text.toString()
        var nickname=nameTv.text.toString()
        if("".equals(msg)){
            return
        }
        var message=Message(msg, Date(),"",auth.currentUser?.email!!,nickname)
        messageEt.setText("")   //메세지 입력창 초기화

        firestore?.collection("message").document().set(message)
            .addOnCompleteListener { task->
                if(!task.isSuccessful){
                    Toast.makeText(this,"네트워크가 원활하지 않습니다", Toast.LENGTH_SHORT).show()
                }

            }
    }
}