package com.visualeap.aliforreddit.presentation.frontPage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.airbnb.epoxy.EpoxyRecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ncapdevi.fragnav.FragNavController
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDispose
import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.presentation.postDetail.PostDetailFragment
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_home.view.*
import javax.inject.Inject

class FrontPageFragment : Fragment(), FrontPageLauncher {
    @Inject
    lateinit var presenter: FrontPagePresenter

    @Inject
    lateinit var fragNavController: FragNavController

    private lateinit var epoxyController: FrontPageEpoxyController
    private lateinit var recyclerView: EpoxyRecyclerView
    private val scopeProvider by lazy { AndroidLifecycleScopeProvider.from(this) }

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
        presenter.start()
            .autoDispose(scopeProvider)
            .subscribe(::render)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    private fun render(viewState: FrontPageViewState) {
        when (viewState) {
            FrontPageViewState.Loading -> ""
            is FrontPageViewState.Failure -> {
                epoxyController.isLoading = false
                epoxyController.requestModelBuild()
                Snackbar.make(recyclerView, viewState.error, Snackbar.LENGTH_SHORT).show()
            }
            is FrontPageViewState.Success -> {
                if (recyclerView.adapter == null) {
                    recyclerView.setController(epoxyController)
                }
                epoxyController.isLoading = viewState.isLoading
                epoxyController.posts = viewState.posts
                epoxyController.requestModelBuild()
            }
        }
    }

    val feed: String
        get() = arguments?.getString(ARG_FEED)
            ?: throw IllegalStateException("Use the newInstance method to instantiate this fragment.")

    companion object {
        private const val ARG_FEED = "feed"

        fun newInstance(feed: String) = FrontPageFragment().apply {
            arguments = bundleOf(ARG_FEED to feed)
        }
    }
}
