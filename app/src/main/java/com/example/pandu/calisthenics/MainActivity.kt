package com.example.pandu.calisthenics

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.pandu.calisthenics.R.id.navigation_home
import com.example.pandu.calisthenics.R.id.navigation_profile
import com.example.pandu.calisthenics.R.id.navigation_activities
import com.example.pandu.calisthenics.menu.task.TaskFragment
import com.example.pandu.calisthenics.menu.DashboradFragment
import com.example.pandu.calisthenics.menu.profile.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_navigation.setOnNavigationItemSelectedListener {item ->
            when (item.itemId) {
                navigation_home -> {
                    loadDashboardFragment(savedInstanceState)
                }
                navigation_activities -> {
                    loadActivitiesFragment(savedInstanceState)
                }
                navigation_profile -> {
                    loadProfileFragment(savedInstanceState)
                }

            }
            true
        }
        bottom_navigation.selectedItemId = navigation_activities
    }

    private fun loadProfileFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, ProfileFragment(), ProfileFragment::class.java.simpleName)
                    .commit()
        }
    }

    private fun loadActivitiesFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container,
                        TaskFragment(), TaskFragment::class.java.simpleName)
                    .commit()
        }
    }

    private fun loadDashboardFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, DashboradFragment(), DashboradFragment::class.java.simpleName)
                    .commit()
        }
    }
}
