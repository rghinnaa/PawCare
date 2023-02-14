package com.example.pawcare.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.pawcare.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    companion object {
        fun Fragment.reInflateMainNavGraph() {
            val fragmentHost = activity?.supportFragmentManager
                ?.findFragmentById(R.id.mainNavHostContainer)

            val navHost = fragmentHost as? NavHostFragment

            val navController = navHost?.navController
            val navInflater = navController?.navInflater

            navInflater?.inflate(R.navigation.nav_auth)
                ?.apply { setStartDestination(R.id.main_fragment) }
                ?.let { navController.graph = it }
        }
    }

}