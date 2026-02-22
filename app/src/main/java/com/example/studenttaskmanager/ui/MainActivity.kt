package com.example.studenttaskmanager.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.studenttaskmanager.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Якщо ці рядки червоні - перевірте android:id в activity_main.xml
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        val fabAddTask = findViewById<FloatingActionButton>(R.id.fabAddTask)

        if (savedInstanceState == null) {
            replaceFragment(TaskListFragment())
        }

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_tasks -> { // Перевірте id у вашому res/menu/bottom_nav_menu.xml
                    replaceFragment(TaskListFragment())
                    true
                }
                R.id.nav_calendar -> {
                    replaceFragment(CalendarFragment())
                    true
                }
                else -> false
            }
        }

        fabAddTask.setOnClickListener {
            startActivity(Intent(this, AddEditTaskActivity::class.java))
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment) // ID має бути у FrameLayout в activity_main.xml
            .commit()
    }
}