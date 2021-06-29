package com.yjr.meeting

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class FirstActivity: AppCompatActivity() {
    lateinit var first_login:Button
    lateinit var first_sign_in: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        first_login = findViewById(R.id.first_login)
        first_sign_in = findViewById(R.id.first_sign_up)

        first_login.setOnClickListener {
            var intent= Intent(this,LoginActivity::class.java) // 인텐트를 이용하여 메인 액티비티로 이동
            startActivity(intent)
        }

        first_sign_in.setOnClickListener {
            var intent2 = Intent(this, SignUpActivity::class.java)
            startActivity(intent2)
        }

    }

}