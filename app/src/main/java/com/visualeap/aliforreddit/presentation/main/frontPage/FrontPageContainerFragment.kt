package com.visualeap.aliforreddit.presentation.main.frontPage

import android.content.Context
import com.google.android.material.tabs.TabLayout
import com.google.android.material.snackbar.Snackbar

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import android.os.Bundle
import android.view.*
import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.presentation.main.DrawerController
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_container_home.*
import kotlinx.android.synthetic.main.fragment_container_home.view.*
import kotlinx.android.synthetic.main.search_bar.view.*
import javax.inject.Inject

class FrontPageContainerFragment : Fragment() {

    @Inject
    lateinit var drawerController: DrawerController

    /**
     * The [androidx.viewpager.widget.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * androidx.fragment.app.FragmentStatePagerAdapter.
     */
    private var sectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_container_home, container, false)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        sectionsPagerAdapter = SectionsPagerAdapter(childFragmentManager)

        // Set up the ViewPager with the sections adapter.
        rootView.container.apply {
            adapter = sectionsPagerAdapter
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(rootView.tabs))
            rootView.tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(this))
        }

        rootView.profileImage.setOnClickListener { _ -> drawerController.toggle() }

        return rootView
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return FrontPageFragment.newInstance(position + 1)
        }

        override fun getCount(): Int {
            // Show 2 total pages.
            return 2
        }
    }
}
