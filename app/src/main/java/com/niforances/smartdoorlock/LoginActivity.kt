package com.niforances.smartdoorlock

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var edtId: TextInputEditText;
    lateinit var edtPwd: TextInputEditText;
    lateinit var btnLogin: Button;
    lateinit var btnResetPwd: Button;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        edtId = findViewById(R.id.edtAccount)
        edtPwd = findViewById(R.id.edtPwd)
        btnLogin = findViewById(R.id.textButton)
        btnResetPwd = findViewById(R.id.btnResetPwd)

        val currentUser = auth.currentUser
        if (currentUser != null) {

            Log.i("log_activity_login","login Complete, userName: ${currentUser.email}")
            moveToMain()
        }

        //로그인 해야 함
        btnLogin.setOnClickListener {
            signIn(edtId.text.toString(), edtPwd.text.toString())
        }

        btnResetPwd.setOnClickListener {
            val intent = Intent(applicationContext, PasswordResetActivity::class.java)
            startActivity(intent)
        }

    }

    private fun createAccount(email: String, password: String) {
        //회원가입
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.i(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.i(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.i("log_activity_login", "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.i("log_activity_login", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "아이디가 존재하지 않거나 비밀번호가 일치하지 않습니다.",
                        Toast.LENGTH_SHORT
                    ).show()

                    edtPwd.setText("")

                    //updateUI(null)
                }
            }
        // [END sign_in_with_email]
    }

    private fun updateUI(user: FirebaseUser?) {
        moveToMain()
    }

    private fun moveToMain() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)

        finish()
    }

    companion object {
        private const val TAG = "EmailPassword"
    }
}