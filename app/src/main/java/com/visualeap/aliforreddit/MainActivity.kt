package com.visualeap.aliforreddit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val tag = MainActivity::class.java.simpleName

    private val onBottomNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, HomeFragmentContainer())
                        .commit();
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_dashboard -> {
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_notifications -> {
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    private val onDrawerItemSelectedListener =
        NavigationView.OnNavigationItemSelectedListener {
            // Handle navigation view item clicks here.
            when (it.itemId) {
                R.id.nav_camera -> {
                    // Handle the camera action
                }
                R.id.nav_gallery -> {

                }
                R.id.nav_slideshow -> {

                }
                R.id.nav_manage -> {

                }
                R.id.nav_share -> {

                }
                R.id.nav_send -> {

                }
            }

            drawer_layout.closeDrawer(GravityCompat.START)
            true
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setSupportActionBar(toolbar)

        navigation.setOnNavigationItemSelectedListener(onBottomNavigationItemSelectedListener)

//        val toggle = ActionBarDrawerToggle(
//            this,
//            drawer_layout,
//            toolbar,
//            R.string.navigation_drawer_open,
//            R.string.navigation_drawer_close
//        )
//        drawer_layout.addDrawerListener(toggle)
//        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(onDrawerItemSelectedListener)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
