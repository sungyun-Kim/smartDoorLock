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
import com.niforances.smartdoorlock.databinding.ActivityMainBinding
import com.niforances.smartdoorlock.fragment.BluetoothFragment
import com.niforances.smartdoorlock.fragment.MainFragment


class MainActivity : AppCompatActivity() {

    //뷰바인더 init
    private lateinit var binding: ActivityMainBinding
    lateinit var fragmentManager: FragmentManager

    //1. BluetoothAdapter 를 가져온다
    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    // isDisabled 임의 정의
    private val BluetoothAdapter.isDisabled: Boolean
        get() = !isEnabled

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //takeIf 문법 : 노션에 정리, 앞의 객체 널체크와 동시에 그 결과에 따른 구문 처리에 용이함

        //아래 구문은 takeIf 절에서
        bluetoothAdapter?.takeIf { it.isDisabled }?.apply {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)

            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                startActivityForResult(enableBtIntent, 1)
            }
        }

        fragmentManager = supportFragmentManager

        fragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.fcvMain, MainFragment())
        }

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


}
