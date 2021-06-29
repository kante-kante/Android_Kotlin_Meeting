package com.yjr.meeting

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity: AppCompatActivity() {
    lateinit var name_et: EditText
    lateinit var email_et2: EditText
    lateinit var password_et2: EditText
    lateinit var sign_up_btn2: Button
    lateinit var move_login: Button
    lateinit var loadingPb2: ProgressBar

    lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        name_et = findViewById(R.id.name_et)
        email_et2 = findViewById(R.id.email_et2)
        password_et2 = findViewById(R.id.password_et2)
        sign_up_btn2 = findViewById(R.id.login_btn)
        loadingPb2 = findViewById(R.id.loading_pb2)
        move_login=findViewById(R.id.move_login)

        auth = FirebaseAuth.getInstance()
        moveLogin(auth.currentUser)
        firestore = FirebaseFirestore.getInstance()

        sign_up_btn2.setOnClickListener {
            var name = name_et.text.toString()
            var email = email_et2.text.toString()
            var password = password_et2.text.toString()

            if (name.equals("")) {
                Toast.makeText(this, "이름을 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (email.equals("")) { //email=="" 과는 다름. ==는 주소값이 같은지를 비교. 문자열 비교 시 equals 사용
                Toast.makeText(this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.length < 6) {
                Toast.makeText(this, "패스워드를 6자 이상 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {

                    if (it.isSuccessful) { // 성공적으로 로그인 된 경우(이메일이 존재하지 않는 경우에서 새로운 이메일로 로그인 할 때)
                        var user =
                            User(auth.currentUser?.email!!) // !! = 항상 존재. 유저의 이메일이 없는 경우는 존재하지 않음

                        firestore.collection("User") // User 컬렉션에 로그인 정보 추가.
                            .document(user.email!!)
                            .set(user)
                            .addOnSuccessListener {
                                firestore.collection("User")
                                    .document(auth.currentUser?.email!!)
                                    .update("name",name)
                                finish()

                                moveLogin(auth.currentUser) // 메인 화면으로 이동.
                            }
                    } else { // 이미 유저 정보가 존재하고 이메일이나 패스워드가 올바르지 않은 경우
                        Toast.makeText(this, "이메일 또는 패스워드가 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
                    }

                }

        }

        move_login.setOnClickListener {
            var intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    fun moveLogin(user: FirebaseUser?){ // 로그인된 사용자 확인
        if(user==null){
            return
        }

        var intent= Intent(this,LoginActivity::class.java) // 인텐트롤 이용하여 메인 액티비티로 이동
        startActivity(intent)
        finish()

        }
}