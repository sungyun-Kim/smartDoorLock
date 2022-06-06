package com.niforances.smartdoorlock

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.niforances.smartdoorlock.databinding.ActivityMainBinding
import com.niforances.smartdoorlock.fragment.BluetoothFragment
import com.niforances.smartdoorlock.fragment.MainFragment


class MainActivity : AppCompatActivity() {

    //뷰바인더 init
    private lateinit var binding: ActivityMainBinding
    lateinit var fragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentManager = supportFragmentManager
        checkCurrentUser()

        //뷰바인딩 초기세팅
        binding = ActivityMainBinding.inflate(layoutInflater)
        val mainView = binding.root

        setContentView(mainView)

        binding.bNVMain.setOnItemSelectedListener { item ->

            //fragment transaction
            when (item.itemId) {
                R.id.page_main -> fragmentManager.commit {
                    setReorderingAllowed(true)
                    replace(R.id.fcvMain, MainFragment())
                }
                R.id.page_bluetooth -> fragmentManager.commit {
                    setReorderingAllowed(true)
                    replace(R.id.fcvMain, BluetoothFragment())
                }
            }
            true // return true;
        }
    }

    private fun checkCurrentUser() {

        FirebaseApp.initializeApp(this)

        val user = Firebase.auth.currentUser
        if (user != null) {
            // User is signed in
            initFragment()
        } else {
            // No user is signed in
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun initFragment() {
        fragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.fcvMain, MainFragment())
        }
    }
}
