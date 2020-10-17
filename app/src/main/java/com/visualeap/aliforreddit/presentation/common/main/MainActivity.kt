package com.visualeap.aliforreddit.presentation.common.main

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.*
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ncapdevi.fragnav.FragNavController
import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.domain.redditor.Redditor
import com.visualeap.aliforreddit.presentation.common.view.drawer.DrawerController
import com.visualeap.aliforreddit.presentation.frontPage.container.FrontPageContainerFragment
import com.visualeap.aliforreddit.presentation.login.BackButtonHandler
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_post_detail.view.*
import java.lang.IllegalStateException
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector,
    MainView,
    FragNavController.RootFragmentListener, FragNavController.TransactionListener,
    DrawerController {
    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var presenter: MainPresenter

    private var savedInstanceState: Bundle? = null

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

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        drawerLayout.setOnApplyWindowInsetsListener { _, insets ->
            // Move bottom navigation above navigation bar
            redditDrawer.updatePadding(bottom = insets.systemWindowInsetBottom)
            bottomBar.updatePadding(bottom = insets.systemWindowInsetBottom)
            insets
        }

        this.savedInstanceState = savedInstanceState

        bottomBar.setOnNavigationItemSelectedListener(
            onBottomNavigationItemSelectedListener
        )

        // Set default tab selection
        if (savedInstanceState == null) {
            bottomBar.selectedItemId = R.id.navigation_home
        }

        fragNavController.rootFragmentListener = this
        fragNavController.transactionListener = this
        fragNavController.initialize(FragNavController.TAB1, savedInstanceState)

        redditDrawer.navigationItemSelectedListener = {
            val message = when (it.id) {
                R.id.navProfile -> "Profile"
                R.id.navSignUp -> "Sign Up"
                R.id.navCoins -> "Coins"
                R.id.navPremium -> "Premium"
                R.id.navSaved -> "Saved"
                R.id.navHistory -> "History"
                R.id.navSettings -> "Settings"
                R.id.navUsername -> "Username"
                R.id.navDarkMode -> "Dark Mode"
                else -> throw IllegalArgumentException("Unknown drawer item")
            }
            Toast.makeText(
                this,
                "You have just cliked on $message",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.start(savedInstanceState == null)
    }

    override fun onStop() {
        super.onStop()
        presenter.stop()
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        this.savedInstanceState = outState
        fragNavController.onSaveInstanceState(outState)
    }

    override val numberOfRootFragments: Int = 1

    override fun getRootFragment(index: Int): Fragment {
        when (index) {
            FragNavController.TAB1 -> return FrontPageContainerFragment()
        }
        throw IllegalStateException("Invalid tab index")
    }

    override fun close() = drawerLayout.closeDrawer(GravityCompat.START)

    override fun lockClosed() = drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

    override fun open() = drawerLayout.openDrawer(GravityCompat.START)

    override fun toggle() = if (drawerLayout.isDrawerOpen(GravityCompat.START)) close() else open()

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

    override fun displayCurrentRedditor(redditor: Redditor) {
        redditDrawer.showRedditorInfo(redditor)
    }

    override fun displayLoginPrompt() {
        redditDrawer.showOfflineMode()
    }

    fun reload() {
        fragNavController.clearStack()
        fragNavController.replaceFragment(FrontPageContainerFragment())
        presenter.start(true)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = androidInjector
}
