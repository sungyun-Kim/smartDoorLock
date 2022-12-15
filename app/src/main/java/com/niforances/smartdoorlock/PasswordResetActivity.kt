package com.niforances.smartdoorlock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PasswordResetActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var btnResetPwd: Button
    lateinit var edtResetEmail: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_reset)

        btnResetPwd = findViewById(R.id.btnResetPwd)
        edtResetEmail = findViewById(R.id.edtResetEmail)

        auth = Firebase.auth

        btnResetPwd.setOnClickListener {
            auth.sendPasswordResetEmail(edtResetEmail.text.toString())
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        Toast.makeText(applicationContext, "비밀번호 초기화 이메일이 전송되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        //Failure
                        Toast.makeText(applicationContext, "비밀번호 초기화 이메일 전송에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
