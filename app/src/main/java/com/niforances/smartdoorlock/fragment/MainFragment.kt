package com.niforances.smartdoorlock.fragment

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.niforances.smartdoorlock.ConnectedThread
import com.niforances.smartdoorlock.R
import java.io.IOException
import java.lang.reflect.Method
import java.util.*


class MainFragment(private val user: FirebaseUser) : Fragment() {


    var BT_MODULE_UUID =
        UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // "random" unique identifier

    lateinit var btnCardSecondRock: Button
    lateinit var tvRoom: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_main, container, false)

        val tvAccount = view.findViewById<TextView>(R.id.tvAccount)
        Log.i("log_Fragment_Main", "id: ${user.email.toString()}")

        val customCardFirst = view.findViewById<View>(R.id.customCardFirst)
        val customCardSecond = view.findViewById<View>(R.id.customCardSecond)

        val tvCardFirstOpen = customCardFirst.findViewById<TextView>(R.id.tvOpenPerm)
        val tvCardFirstRock = customCardFirst.findViewById<TextView>(R.id.tvRockPerm)
        val btnCardFirstOpen = customCardFirst.findViewById<Button>(R.id.btnOpen)
        val btnCardFirstRock = customCardFirst.findViewById<Button>(R.id.btnRock)

        tvRoom = customCardSecond.findViewById<TextView>(R.id.tvRoom)

        tvCardFirstRock.text = "잠금 권한: 있음"

        val tvCardSecondOpen = customCardSecond.findViewById<TextView>(R.id.tvOpenPerm)
        val tvCardSecondRock = customCardSecond.findViewById<TextView>(R.id.tvRockPerm)
        val btnCardSecondOpen = customCardSecond.findViewById<Button>(R.id.btnOpen)
        btnCardSecondRock = customCardSecond.findViewById<Button>(R.id.btnRock)

        var flagOne = true
        var flagSecond = true

        tvCardFirstOpen.visibility = View.GONE
        tvCardFirstRock.visibility = View.GONE
        tvCardSecondOpen.visibility = View.GONE
        tvCardSecondRock.visibility = View.GONE
        btnCardFirstOpen.visibility = View.GONE
        btnCardFirstRock.visibility = View.GONE
        btnCardSecondOpen.visibility = View.GONE
        btnCardSecondRock.visibility = View.GONE

        customCardFirst.setOnClickListener {
            flagOne = !flagOne
            if (flagOne) {
                tvCardFirstOpen.visibility = View.VISIBLE
                tvCardFirstRock.visibility = View.VISIBLE
                btnCardFirstOpen.visibility = View.VISIBLE
                btnCardFirstRock.visibility = View.VISIBLE
            } else {
                tvCardFirstOpen.visibility = View.GONE
                tvCardFirstRock.visibility = View.GONE
                btnCardFirstOpen.visibility = View.GONE
                btnCardFirstRock.visibility = View.GONE
            }
        }

        customCardSecond.setOnClickListener {
            flagSecond = !flagSecond
            if (flagSecond) {
                tvCardSecondOpen.visibility = View.VISIBLE
                tvCardSecondRock.visibility = View.VISIBLE
                btnCardSecondOpen.visibility = View.VISIBLE
            } else {
                tvCardSecondOpen.visibility = View.GONE
                tvCardSecondRock.visibility = View.GONE
                btnCardSecondOpen.visibility = View.GONE
                btnCardSecondRock.visibility = View.GONE
            }
        }

        val tvCSTitle = customCardSecond.findViewById<TextView>(R.id.tvLectureName)
        tvCSTitle.text = "캡스톤디자인1-3_1_B"

        val tvCSTime = customCardSecond.findViewById<TextView>(R.id.tvLectureTime)
        tvCSTime.text = "00:00~24:00"

        val db = Firebase.firestore

        db.collection("users").whereEqualTo("email", user.email.toString()).get()
            .addOnSuccessListener {
                val data = it.documents[0]
                tvAccount.text = "hello ${
                    data.get("firstName")
                } " + data.get("lastName")
            }
            .addOnFailureListener { exception ->
                Log.i("log_fragment", "Error getting documents: ", exception)
            }

        db.collection("todos").whereEqualTo("username", "niforances")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.i("log_fragment", "success: ${document.id} => ${document.data}")

                    // 파싱 후 대입
                    tvCSTitle.setText("캡스톤디자인")
                    tvRoom.setText("실습실(C324)")
                }
            }
            .addOnFailureListener { exception ->
                Log.i("log_fragment", "Error getting documents: ", exception)
            }

        val btAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

        if (ActivityCompat.checkSelfPermission(
                requireActivity().applicationContext,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }


        val device: BluetoothDevice = btAdapter!!.getRemoteDevice("98:D3:32:70:AE:79")

        var flag = true

        var btSocket: BluetoothSocket? = null

        btnCardSecondOpen.setOnClickListener {
            // create & connect socket
            // create & connect socket
            try {
                btSocket = createBluetoothSocket(device) as BluetoothSocket?
                btSocket!!.connect()
            } catch (e: IOException) {
                flag = false
                e.printStackTrace()
            }

            if (flag) {
                val connectedThread = ConnectedThread(btSocket!!)
                connectedThread.start()
                tvRoom.text = "실습실(C324) : 74:F0:7D:C9:9D:21: 출석 인증됨"
                Toast.makeText(requireContext(), "출석 처리 되었습니다.", Toast.LENGTH_SHORT)
                    .show()

                btnCardSecondRock.visibility = View.VISIBLE

                btnCardSecondRock.setOnClickListener {
                    Log.i("fragment","pressed!")
                    connectedThread.write("1")
                }
            }
        }

        return view
    }

    @Throws(IOException::class)
    private fun createBluetoothSocket(device: BluetoothDevice): Any? {
        try {
            val m: Method = device.javaClass.getMethod(
                "createInsecureRfcommSocketToServiceRecord",
                UUID::class.java
            )
            return m.invoke(device, BT_MODULE_UUID)
        } catch (e: Exception) {
            Log.e("fragment", "Could not create Insecure RFComm Connection", e)
        }
        return if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
        } else {
            device.createRfcommSocketToServiceRecord(BT_MODULE_UUID)
            Log.i("fragment", "success")
        }
    }


}