package com.visualeap.aliforreddit.presentation.main.frontPage

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyRecyclerView
import com.ncapdevi.fragnav.FragNavController
import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.domain.model.Post
import com.visualeap.aliforreddit.domain.util.Mapper
import com.visualeap.aliforreddit.presentation.main.frontPage.container.FrontPageContainerFragment
import com.visualeap.aliforreddit.presentation.main.postDetail.PostDetailFragment
import com.visualeap.aliforreddit.presentation.model.PostView
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_home.view.frontPageRecyclerView
import javax.inject.Inject

class FrontPageFragment : Fragment(), FrontPageView {
    @Inject
    lateinit var presenter: FrontPagePresenter

    @Inject
    lateinit var fragNavController: FragNavController

    @Inject
    lateinit var postViewMapper: Mapper<PostView, Post>

    private lateinit var epoxyController: FrontPageEpoxyController
    private lateinit var recyclerView: EpoxyRecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        epoxyController = FrontPageEpoxyController(postViewMapper) {
            //TODO Refactor, move to presenter
            fragNavController.pushFragment(PostDetailFragment.newInstance(it))
        }

        recyclerView = rootView.frontPageRecyclerView
        recyclerView.setItemSpacingDp(8) //TODO Make this a constant

        return rootView
    }

    override fun onStart() {
        super.onStart()
        if (sectionNumber == FrontPageContainerFragment.POPULAR_SECTION_NUMBER)
            presenter.start()
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onStop() {
        super.onStop()
        if (sectionNumber == FrontPageContainerFragment.POPULAR_SECTION_NUMBER) {
            presenter.stop()
        }
    }

    override fun displayPosts(posts: PagedList<Post>) {
        if (recyclerView.adapter == null) {
            recyclerView.setController(epoxyController)
        }
        epoxyController.submitList(posts)
    }

    private val sectionNumber: Int
        get() = arguments?.getInt(ARG_SECTION_NUMBER)
            ?: throw IllegalStateException("Use the newInstance method to instantiate this fragment.")

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        fun newInstance(sectionNumber: Int) = FrontPageFragment().apply {
            arguments = bundleOf(ARG_SECTION_NUMBER to sectionNumber)
        }

        private const val LAYOUT_MANGER_STATE_KEY = "layout_manger_state"
    }
}
