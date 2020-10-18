package com.visualeap.aliforreddit.presentation.postDetail


import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.updatePadding
import com.google.android.material.snackbar.Snackbar
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDispose

import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.presentation.common.util.makeSnackbar
import com.visualeap.aliforreddit.presentation.common.view.drawer.DrawerController
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_post_detail.*
import kotlinx.android.synthetic.main.fragment_post_detail.view.*

class PostDetailFragment : Fragment(), PostDetailLauncher {
    @Inject
    lateinit var presenter: PostDetailPresenter

    @Inject
    lateinit var drawerController: DrawerController

    private lateinit var epoxyController: PostDetailEpoxyController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_post_detail, container, false)
        drawerController.lockClosed()

        rootView.setOnApplyWindowInsetsListener { _, insets ->
            // Move toolbar below status bar
            appBarLayout.updatePadding(top = insets.systemWindowInsetTop)
            insets
        }

        (activity as AppCompatActivity).apply {
            setSupportActionBar(rootView.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.viewState
            .observeOn(AndroidSchedulers.mainThread())
            .autoDispose(AndroidLifecycleScopeProvider.from(viewLifecycleOwner))
            .subscribe(::render)

        epoxyController = PostDetailEpoxyController()
        postDetailRecyclerView.apply {
            setController(epoxyController)
            addItemDecoration(
                CommentItemDecoration(resources.getDimension(R.dimen.comment_spacing).toInt())
            )
        }

        presenter.passEvent(PostDetailEvent.ScreenLoadEvent)
    }

    private fun render(viewState: PostDetailViewState) {
        //Display post
        epoxyController.postLoading = viewState.postLoading
        viewState.post?.let {
            appBarLayout.setBackgroundColor(Color.parseColor(it.subredditColor))
            epoxyController.post = it
        }
        viewState.error?.let { requireView().makeSnackbar(it).show() }

        //Display comments
        epoxyController.commentsLoading = viewState.commentsLoading
        viewState.comments?.let { epoxyController.comments = it }
        viewState.commentsError?.let { epoxyController.commentsError = true }
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onCleared()
    }

    val postId: String
        get() = arguments?.getString(ARG_POST_ID)
            ?: throw IllegalStateException("Use the newInstance method to instantiate this fragment.")

    val subreddit: String
        get() = arguments?.getString(ARG_SUBREDDIT)
            ?: throw IllegalStateException("Use the newInstance method to instantiate this fragment.")

    companion object {
        private const val ARG_POST_ID = "post_id"
        private const val ARG_SUBREDDIT = "subreddit"

        fun newInstance(postId: String, subreddit: String) = PostDetailFragment().apply {
            arguments = bundleOf(ARG_POST_ID to postId, ARG_SUBREDDIT to subreddit)
        }
    }
}
