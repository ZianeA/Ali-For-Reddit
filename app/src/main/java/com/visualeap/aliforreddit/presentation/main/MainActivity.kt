package com.visualeap.aliforreddit.presentation.main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.ncapdevi.fragnav.FragNavController
import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.presentation.main.frontPage.container.FrontPageContainerFragment
import com.visualeap.aliforreddit.presentation.main.login.BackButtonHandler
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.IllegalStateException
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector, MainView,
    FragNavController.RootFragmentListener, FragNavController.TransactionListener,
    DrawerController {
    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Fragment>

    val fragNavController: FragNavController =
        FragNavController(supportFragmentManager, R.id.fragment_container)

    private val onBottomNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
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

        bottomNavigationView.setOnNavigationItemSelectedListener(
            onBottomNavigationItemSelectedListener
        )

        // Set default tab selection
        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId = R.id.navigation_home
        }

        drawerNavigationView.setNavigationItemSelectedListener(onDrawerItemSelectedListener)

        fragNavController.rootFragmentListener = this
        fragNavController.transactionListener = this
        fragNavController.initialize(FragNavController.TAB1, savedInstanceState)

        //TODO this is temporally
        /*profileImage.setOnClickListener {
            startActivityForResult(
                Intent(this, LoginActivity::class.java), REQUEST_CODE_LOGIN
            )
//            drawerLayout.openDrawer(GravityCompat.START)
        }*/
    }

    override fun onBackPressed() {
        val backButtonHandler = fragNavController.currentFrag as? BackButtonHandler
        if (backButtonHandler != null && backButtonHandler.onBackPressed()) {
            return
        }

        when {
            drawerLayout.isDrawerOpen(GravityCompat.START) -> drawerLayout.closeDrawer(GravityCompat.START)
            fragNavController.isRootFragment.not() -> fragNavController.popFragment()
            else -> super.onBackPressed()
        }
    }

    //handle up button
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            fragNavController.popFragment()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        fragNavController.onSaveInstanceState(outState)
    }

    override val numberOfRootFragments: Int
        get() = 1

    override fun getRootFragment(index: Int): Fragment {
        when (index) {
            FragNavController.TAB1 -> return FrontPageContainerFragment()
        }
        throw IllegalStateException("Invalid tab index")
    }

    override fun close() {
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    override fun lockClosed() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    override fun open() {
        drawerLayout.openDrawer(GravityCompat.START)
    }

    override fun toggle() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) close() else open()
    }

    override fun onFragmentTransaction(
        fragment: Fragment?,
        transactionType: FragNavController.TransactionType
    ) {
        // If we have a backstack, show the back button
        supportActionBar?.setDisplayHomeAsUpEnabled(fragNavController.isRootFragment.not())
    }

    override fun onTabTransaction(fragment: Fragment?, index: Int) {
        // If we have a backstack, show the back button
        supportActionBar?.setDisplayHomeAsUpEnabled(fragNavController.isRootFragment.not())
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = androidInjector

    companion object {
        private const val REQUEST_CODE_LOGIN = 101
    }
}
