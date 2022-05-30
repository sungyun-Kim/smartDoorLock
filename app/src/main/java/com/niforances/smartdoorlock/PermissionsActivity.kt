package com.niforances.smartdoorlock

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.niforances.smartdoorlock.databinding.ActivityPermissionsBinding

class PermissionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPermissionsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permissions)

        //permissionsActivity view binding
        binding = ActivityPermissionsBinding.inflate(layoutInflater)
        val permissionView = binding.root

        setContentView(permissionView)

        binding.btnPermissionsBluetooth.setOnClickListener {

            if (isPermissionsGranted()) {

                val intent = Intent(this, MainActivity::class.java)

                startActivity(intent)
                finish()
            } else {

                Toast.makeText(applicationContext, "권한 허용이 필요합니다.", Toast.LENGTH_SHORT).show()

            }

        }

        //각종 권한을 받는 액티비티
    }

    private fun isPermissionsGranted(): Boolean {

        return false
    }

    override fun onResume() {
        super.onResume()


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


    }
}