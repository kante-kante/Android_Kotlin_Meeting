package com.yjr.meeting

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class FindPasswordActivity: AppCompatActivity() {
    lateinit var send_pwd_btn: Button
    lateinit var find_email_et: EditText
    lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_pwd)

        auth= FirebaseAuth.getInstance()
        find_email_et=findViewById(R.id.find_email_et)
        send_pwd_btn=findViewById(R.id.send_pwd_btn)

        send_pwd_btn.setOnClickListener {
            var email=find_email_et.text.toString()

            if(email.equals("")){
                Toast.makeText(this,"이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
            }else {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                this,
                                "해당 이메일로 비밀번호 재설정 메일을 보냈습니다. 메일함을 확인해주세요.",
                                Toast.LENGTH_SHORT
                            ).show()

                            var intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "해당 계정이 존재하지 않습니다. 아이디를 다시 확인해주세요.", Toast.LENGTH_SHORT)
                                .show()
                            return@addOnCompleteListener
                        }
                    }

                var intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }



}