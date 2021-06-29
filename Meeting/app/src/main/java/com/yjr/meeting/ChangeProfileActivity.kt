package com.yjr.meeting

import android.app.Activity
import android.content.Intent
import android.content.Intent.ACTION_PICK
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*


class ChangeProfileActivity:AppCompatActivity() {

    lateinit var profileIv:ImageView
    lateinit var nameEt:EditText
    lateinit var modifyBtn:Button

    lateinit var profile_et:EditText
    lateinit var profile_et2:EditText

    //인증을 가져오기 위한 auth
    lateinit var auth:FirebaseAuth
    lateinit var storage:FirebaseStorage
    lateinit var firestore:FirebaseFirestore

    //request code
    val REQ_IMAGE=1000

    //
    var selectedImage:Uri?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_change_profile)
        profileIv = findViewById(R.id.profile_iv)
        nameEt = findViewById(R.id.name_et)
        modifyBtn = findViewById(R.id.modify_btn)

        profile_et = findViewById(R.id.profile_et)
        profile_et2= findViewById(R.id.profile_et2)

        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        firestore = FirebaseFirestore.getInstance()
        //getinstance 로 가져오는 이유..? auth라는 인스턴스를 같은 인스턴스를 가리키고 있어야만하기 때문에 파이어베이스 어스를 사용할 때마다. 새로 생성해서 부여해준다고 한다면 각각 다른 인스턴스를 가리키게 된다.
        // 파이어베이스는 이러한 패턴을 유지하게 한다 싱글 텀 페턴 한 프로그렘 내에서 한개의 인스턴스로 모든 기능을 커버허기 위해서. 안드로이드에서 데이터베이스에 연결을 한다고 한다면 싱글톤 겟 인스턴스를 통해서
        //한번만생성하고 이를 가져다가 쓰는 패턴을 유지한다. 파이어베이스에서만 쓰는게 아니라 프로그래밍에서 자주 쓰인다.


        //프로필을 클릭했을 때
        profileIv.setOnClickListener {
            var intent = Intent(ACTION_PICK)
            intent.type = "image/*" // 타입을 이미지로 지정할수 있게끔.
            startActivityForResult(intent, REQ_IMAGE) //리퀘스트 코드를 넣어줘야한다.
        }


        //<수정하기 버튼 클릭>이미지를 올리는 작업
        modifyBtn.setOnClickListener {
            if(selectedImage!=null){
                //파일의 이름을 먼저 정의
                var filename=UUID.randomUUID().toString()//랜덤한 UUID값을 넣어준다. UUID는 시간과 난수값으로 매번 다른값이 나오도록해준다.
                storage.getReference("profile").child(filename).putFile(selectedImage!!)
                        .addOnSuccessListener {
                            //업로드한 이미지에 실제로 접속 할 수있는 링크를 얻어와야한다.
                            it.metadata?.reference?.downloadUrl?.addOnSuccessListener {
                                var profileUrl=it.toString()
                                var user=User()
                                user.email=auth.currentUser?.email!!
                                user.name=nameEt.text.toString()
                                user.profiletext=profile_et.text.toString()
                                user.profile_text2=profile_et2.text.toString()

                                user.profileUrl=profileUrl //업로드한 이미질ㄹ 접속할 수있는경로로 지정
                                firestore.collection("User").document(auth.currentUser?.email!!) //현제 접속해있는 사용자 email로 유저 콜렉션에서 찾아서 설정
                                        .set(user)
                                //이젠 엑티비티가 있을 필요가 없기 때문에
                                        .addOnSuccessListener {
                                            setResult(Activity.RESULT_OK) //사용자가 프로필을 바꾸고 나서 리턴이 되는구나.
                                            finish()
                                        }
                            }
                        }
            }

            else //이름만 올릴려고 하는경우
            {
                var name=nameEt.text.toString()

                var profile= profile_et.text.toString()
                var profile2= profile_et2.text.toString()
                if(name.length==0){ //이름을 타이핑 안 했을 때
                    Toast.makeText(this,"이름을 입력해 주세요",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                else{ //이름만 올리는것이기 때문에 바로 User 에 추가.
                    firestore.collection("User").document(auth.currentUser?.email!!)
                            .update("name",name,"profiletext",profile,"profile_text2",profile2) //t수정정
                           .addOnSuccessListener {
                                setResult(Activity.RESULT_OK) //사용자가 프로필을 바꾸고 나서 리턴이 되는구나.
                                finish()
                            }
                }
            }

        }

    }


        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == REQ_IMAGE && resultCode == Activity.RESULT_OK) {
                //사용자가 클릭
                selectedImage = data?.data
                profileIv.setImageURI(selectedImage)
            }


        }


    }





