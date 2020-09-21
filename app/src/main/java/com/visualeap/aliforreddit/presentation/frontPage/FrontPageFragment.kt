package com.visualeap.aliforreddit.presentation.frontPage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ncapdevi.fragnav.FragNavController
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDispose
import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.presentation.common.util.dpToPx
import com.visualeap.aliforreddit.presentation.common.util.showIf
import com.visualeap.aliforreddit.presentation.postDetail.PostDetailFragment
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

class FrontPageFragment : Fragment(), FrontPageLauncher {
    @Inject
    lateinit var presenter: FrontPagePresenter

    @Inject
    lateinit var fragNavController: FragNavController

    private val epoxyController = FrontPageEpoxyController()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.viewState
            .autoDispose(AndroidLifecycleScopeProvider.from(viewLifecycleOwner))
            .subscribe(::render)

        epoxyController.apply {
            /*onBindPostListener =
                { presenter.passEvent(FrontPageEvent.PostBoundEvent(it)) }*/

            onPostClickListener = {
                fragNavController.pushFragment(
                    PostDetailFragment.newInstance(it.id, it.subreddit.removePrefix("r/"))
                )
            }

            adapter.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT
        }

        frontPageRecyclerView.setItemSpacingDp(8)
        frontPageRecyclerView.setController(epoxyController)
        frontPageRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(epoxyController.hasPendingModelBuild()) return
                val scrollDistance = dpToPx(requireContext(), 1)

                if (dy > scrollDistance) {
                    val lastVisibleItemPosition =
                        (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    presenter.passEvent(
                        FrontPageEvent.PostBoundEvent(
                            lastVisibleItemPosition,
                            true
                        )
                    )
                } else if(dy < -scrollDistance) {
                    val firstVisibleItemPosition =
                        (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    presenter.passEvent(
                        FrontPageEvent.PostBoundEvent(
                            firstVisibleItemPosition,
                            false
                        )
                    )
                }
            }
        })
        presenter.passEvent(FrontPageEvent.ScreenLoadEvent)
    }

    private fun render(viewState: FrontPageViewState) {
        progressBar.showIf { viewState.loading }
        epoxyController.isLoadingMore = viewState.loadingMore
        epoxyController.posts = viewState.posts
        viewState.error?.let {
            Snackbar.make(frontPageRecyclerView, viewState.error, Snackbar.LENGTH_SHORT).show()
        }
        epoxyController.adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.ALLOW
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onCleared()
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
