package com.visualeap.aliforreddit.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.presentation.frontPage.FrontPageContainerFragment
import com.visualeap.aliforreddit.presentation.login.LoginActivity
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = androidInjector

    private val tag = MainActivity::class.java.simpleName

    private val onBottomNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.fragment_container,
                            FrontPageContainerFragment()
                        )
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

            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setSupportActionBar(toolbar)

        bottomNavigationView.setOnNavigationItemSelectedListener(
            onBottomNavigationItemSelectedListener
        )

        // Set default selection
        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId = R.id.navigation_home
        }

//        val toggle = ActionBarDrawerToggle(
//            this,
//            drawer_layout,
//            toolbar,
//            R.string.navigation_drawer_open,
//            R.string.navigation_drawer_close
//        )
//        drawer_layout.addDrawerListener(toggle)
//        toggle.syncState()

        drawerNavigationView.setNavigationItemSelectedListener(onDrawerItemSelectedListener)

        //TODO this is temporally
        profileImage.setOnClickListener {
            startActivityForResult(
                Intent(this, LoginActivity::class.java),
                REQUEST_CODE_LOGIN
            )
        }
    }

    companion object {
        private const val REQUEST_CODE_LOGIN = 101
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
