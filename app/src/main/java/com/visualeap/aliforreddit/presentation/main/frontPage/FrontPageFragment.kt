package com.visualeap.aliforreddit.presentation.main.frontPage

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyRecyclerView
import com.ncapdevi.fragnav.FragNavController
import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.domain.model.feed.DefaultFeed
import com.visualeap.aliforreddit.domain.model.Post
import com.visualeap.aliforreddit.domain.util.Mapper
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

        epoxyController = FrontPageEpoxyController(presenter::onPostBound) {
            fragNavController.pushFragment(PostDetailFragment.newInstance(/*it*/))
        }

        recyclerView = rootView.frontPageRecyclerView
        recyclerView.setItemSpacingDp(8) //TODO Make this a constant

        return rootView
    }

    override fun onStart() {
        super.onStart()
        presenter.start(feed)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onStop() {
        super.onStop()
        presenter.stop()
    }

    override fun displayPosts(posts: List<FeedPostDto>) {
        if (recyclerView.adapter == null) {
            recyclerView.setController(epoxyController)
        }
        epoxyController.posts = posts
        epoxyController.requestModelBuild()
    }

    private val feed: DefaultFeed
        get() = arguments?.getSerializable(ARG_FEED) as? DefaultFeed
            ?: throw IllegalStateException("Use the newInstance method to instantiate this fragment.")

    companion object {
        private const val ARG_FEED = "feed"

        fun newInstance(feed: DefaultFeed) = FrontPageFragment().apply {
            arguments = bundleOf(ARG_FEED to feed)
        }
    }
}
