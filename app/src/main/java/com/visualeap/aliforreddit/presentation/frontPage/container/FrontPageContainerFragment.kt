package com.visualeap.aliforreddit.presentation.frontPage.container

import android.content.Context
import com.google.android.material.tabs.TabLayout

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import android.os.Bundle
import android.view.*
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.viewpager.widget.ViewPager
import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.domain.feed.DefaultFeed
import com.visualeap.aliforreddit.presentation.common.view.drawer.DrawerController
import com.visualeap.aliforreddit.presentation.frontPage.FrontPageFragment
import com.visualeap.aliforreddit.presentation.login.BackButtonHandler
import com.visualeap.aliforreddit.presentation.login.LoginFragment
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_container_home.*
import kotlinx.android.synthetic.main.fragment_container_home.view.*
import javax.inject.Inject

class FrontPageContainerFragment : Fragment(), FrontPageContainerView, BackButtonHandler {
    @Inject
    lateinit var drawerController: DrawerController

    @Inject
    lateinit var presenter: FrontPageContainerPresenter

    private lateinit var mViewPager: ViewPager
    private var loginFragment: LoginFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_container_home, container, false)
        rootView.setOnApplyWindowInsetsListener { _, insets ->
            // Move toolbar below status bar
            appBarLayout.updatePadding(top = insets.systemWindowInsetTop)
            insets
        }

        rootView.viewPager.apply {
            mViewPager = this
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(rootView.tabs))
            rootView.tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(this))
        }

        rootView.profileImage.setOnClickListener { _ -> drawerController.toggle() }

        return rootView
    }

    override fun onStart() {
        super.onStart()
        presenter.start()
    }

    override fun onStop() {
        super.onStop()
        presenter.stop()
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun showLoginScreen() {
        loginFragment = LoginFragment()
        mViewPager.adapter = SectionsPagerAdapter(
            childFragmentManager,
            loginFragment!!,
            FrontPageFragment.newInstance(DefaultFeed.Popular.name)
        )
    }

    override fun showHomeScreen() {
        mViewPager.adapter = SectionsPagerAdapter(
            childFragmentManager,
            FrontPageFragment.newInstance(DefaultFeed.Home.name),
            FrontPageFragment.newInstance(DefaultFeed.Popular.name)
        )
    }

    override fun onBackPressed() =
        // TODO handle back button only if the current selected tab is Home tab
        if (loginFragment != null) (loginFragment as BackButtonHandler).onBackPressed() else false

    /**
     * The [androidx.viewpager.widget.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * androidx.fragment.app.FragmentStatePagerAdapter.
     */

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(
        fm: FragmentManager,
        private val firstSectionFragment: Fragment,
        private val secondSectionFragment: Fragment
    ) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            return when (position) {
                0 -> firstSectionFragment
                1 -> secondSectionFragment
                else -> throw IllegalStateException("Invalid tab position")
            }
        }

        override fun getCount(): Int {
            // Show 2 total pages.
            return 2
        }
    }
}
