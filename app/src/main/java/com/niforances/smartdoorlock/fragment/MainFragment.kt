package com.niforances.smartdoorlock.fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.niforances.smartdoorlock.R

class MainFragment(private val user: FirebaseUser) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_main, container, false)

        val tvAccount = view.findViewById<TextView>(R.id.tvAccount)
        Log.i("log_Fragment_Main", "id: ${user.email.toString()}")
        tvAccount.text = "hello ${user.email.toString()}"

        val db = Firebase.firestore
        

        return view
    }

}