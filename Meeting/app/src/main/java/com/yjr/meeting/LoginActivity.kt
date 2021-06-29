package com.yjr.meeting

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore


class LoginActivity : AppCompatActivity() {
    lateinit var emailEt: EditText
    lateinit var passwordEt: EditText
    lateinit var loginBtn: Button
    lateinit var move_signUp:Button
    lateinit var loadingPb: ProgressBar

    lateinit var find_password_tv: TextView

    lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailEt=findViewById(R.id.email_et2)
        passwordEt=findViewById(R.id.password_et2)
        loginBtn=findViewById(R.id.login_btn)
        move_signUp=findViewById(R.id.move_signup)
        loadingPb=findViewById(R.id.loading_pb2)

        find_password_tv=findViewById(R.id.find_password_tv)

        auth= FirebaseAuth.getInstance()
        moveMain(auth.currentUser) // 하단부 moveMain 함수 확인. FirebaseAuth에서 현재 유저 정보 확인.
        firestore= FirebaseFirestore.getInstance()

        loginBtn.setOnClickListener { // 로그인 버튼 클릭 시
            var email=emailEt.text.toString()
            var password=passwordEt.text.toString()

            if(email.equals("")){ //email=="" 과는 다름. ==는 주소값이 같은지를 비교. 문자열 비교 시 equals 사용
                Toast.makeText(this,"이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(password.length<6){
                Toast.makeText(this,"패스워드를 6자 이상 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            startLoading() // 네트워크 작업이 시작되는 부분에서 로딩 표시
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                stopLoading() // 파이어베이스 호출 후 종료
                if(it.isSuccessful){
                    moveMain(auth.currentUser)
                }
                else{
                    Toast.makeText(this,"가입된 정보가 없습니다. 회원가입을 진행해 주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        move_signUp.setOnClickListener {
            var intent=Intent(this,SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        find_password_tv.setOnClickListener {
            var intent2=Intent(this,FindPasswordActivity::class.java)
            startActivity(intent2)
            finish()
        }


    }



    fun moveMain(user: FirebaseUser?){ // 로그인된 사용자 확인
        if(user==null){
            return
        }

        var intent= Intent(this,MainActivity::class.java) // 인텐트롤 이용하여 메인 액티비티로 이동
        startActivity(intent)
        finish()

    }
    fun startLoading(){
        loadingPb.visibility= View.VISIBLE
    }
    fun stopLoading(){
        loadingPb.visibility= View.GONE
    }
}