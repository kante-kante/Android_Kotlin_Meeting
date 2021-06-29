package com.yjr.meeting

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import de.hdodenhof.circleimageview.CircleImageView


class ProfileFragment: Fragment() {

    //위젯변수 선언
    lateinit var profileIv:CircleImageView
    lateinit var nameTv:TextView
    lateinit var changeProfileBtn:Button
    lateinit var photoListRv:RecyclerView
    lateinit var logoutBtn:Button
    lateinit var auth:FirebaseAuth //로그인한 사용자
    lateinit var firestore:FirebaseFirestore //가저올 저옵

    lateinit var postList:ArrayList<Post> //이후 수업떄 가져옴
    lateinit var profileAdapter:ProfileAdapter

    lateinit var  profile_text:TextView
    lateinit var  profile_text2:TextView

    //상수

    val REQ_CHANGE_PROFILE= 2000
    //초기화 시키는 온크레이트 문
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view=inflater.inflate(R.layout.fragment_profile,container,false)

        profileIv=view.findViewById(R.id.profile_iv)
        nameTv=view.findViewById(R.id.name_tv)
        changeProfileBtn=view.findViewById(R.id.change_profile_btn)
        photoListRv=view.findViewById(R.id.photo_list_rv)
        logoutBtn=view.findViewById(R.id.logout_btn)

        profile_text2=view.findViewById(R.id.profile_text2)
        profile_text=view.findViewById(R.id.profile_text) //
        //뷰를 가져오고나서 리소스도 초기화 해준다.

        auth= FirebaseAuth.getInstance()
        firestore= FirebaseFirestore.getInstance()



        return view;
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //프로필을 변경 할 수있는 엑티비티 > button

        postList=ArrayList<Post>() //어뎁터와 리사이클러뷰 설정
        profileAdapter=ProfileAdapter(activity!!,postList)

        photoListRv.adapter=profileAdapter
        photoListRv.layoutManager=GridLayoutManager(activity!!,3)

        firestore.collection("Post") // 이후 수업떄 가져옴
                .orderBy("uploadDate",Query.Direction.ASCENDING)
                .whereEqualTo("userId",auth.currentUser?.email)
                .addSnapshotListener { value, error ->
                    if(value!=null){
                        for(dc in value.documentChanges){
                            if(dc.type==DocumentChange.Type.ADDED){
                                var post=dc.document.toObject(Post::class.java)
                                postList.add(0,post)
                            }
                        }
                        profileAdapter.notifyDataSetChanged()
                    }
                }

        changeProfileBtn.setOnClickListener {
            var intent= Intent(activity,ChangeProfileActivity::class.java)
           // startActivity(intent) 바뀐값을 적용해주려면 이것은 안된다.
            startActivityForResult(intent,REQ_CHANGE_PROFILE)

        }
        //로그아웃 버튼 눌렀을 때
        logoutBtn.setOnClickListener {
            //firebase auth에서 sign out 기능 호출

            auth.signOut()
            var intent=Intent(activity,LoginActivity::class.java) //로그인 페이지 이동
            startActivity(intent)
            activity?.finish()
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
                            profile_text.text=user?.profiletext
                            profile_text2.text=user?.profile_text2

                            if(user?.profileUrl!=null) {
                                Glide.with(profileIv).load(user?.profileUrl).into(profileIv)
                            }
                        }
                    }
        }







    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==REQ_CHANGE_PROFILE&&resultCode==RESULT_OK){ //사용자가 프로필을 바꾼 상황
            updateProfile()

                        }
                    }

        }





