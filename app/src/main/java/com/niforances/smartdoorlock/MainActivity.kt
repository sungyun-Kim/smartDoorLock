package com.niforances.smartdoorlock

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.niforances.smartdoorlock.databinding.ActivityMainBinding
import com.niforances.smartdoorlock.fragment.BluetoothFragment
import com.niforances.smartdoorlock.fragment.MainFragment
import com.niforances.smartdoorlock.fragmentFactory.MainFragmentFactory


class MainActivity : AppCompatActivity() {

    //뷰바인더 init
    private lateinit var binding: ActivityMainBinding

    private lateinit var fragmentManager: FragmentManager
    private var user = Firebase.auth.currentUser
    private lateinit var mainFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        checkCurrentUser()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainFragment = supportFragmentManager.fragmentFactory.instantiate(
            classLoader,
            MainFragment::class.java.name
        )

        initFragment()

        //뷰바인딩 초기세팅
        binding = ActivityMainBinding.inflate(layoutInflater)
        val mainView = binding.root

        setContentView(mainView)

        binding.bNVMain.setOnItemSelectedListener { item ->

            //fragment transaction
            when (item.itemId) {
                R.id.page_main -> supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace(R.id.fcvMain, mainFragment)
                }
                R.id.page_bluetooth -> supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace(R.id.fcvMain, BluetoothFragment())
                }
            }
            true // return true;
        }
    }

    private fun checkCurrentUser() {

        FirebaseApp.initializeApp(this)
        if (user == null) {
            //로그인 세션이 만료되어 있으면 로그인 화면으로 돌아가기
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            supportFragmentManager.fragmentFactory = MainFragmentFactory(user!!)
            Log.i("log_activity_main", "user: ${user!!.email}")
        }
    }

    private fun initFragment() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.fcvMain, mainFragment)
        }

    }
}
