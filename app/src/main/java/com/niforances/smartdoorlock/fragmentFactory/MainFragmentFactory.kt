package com.niforances.smartdoorlock.fragmentFactory

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.google.firebase.auth.FirebaseUser
import com.niforances.smartdoorlock.fragment.MainFragment

class MainFragmentFactory(private val user: FirebaseUser?) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            MainFragment::class.java.name -> MainFragment(user!!)
            else -> super.instantiate(classLoader, className)
        }
    }
}