package com.visualeap.aliforreddit.presentation.main.postDetail


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.paging.PagedList
import com.ncapdevi.fragnav.FragNavController

import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.domain.model.Comment
import com.visualeap.aliforreddit.domain.model.Post
import com.visualeap.aliforreddit.domain.model.Subreddit
import com.visualeap.aliforreddit.presentation.model.CommentView
import com.visualeap.aliforreddit.presentation.model.PostView
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_post_detail.view.*
import javax.inject.Inject

class PostDetailFragment : Fragment(), PostDetailView {
    @Inject
    lateinit var presenter: PostDetailPresenter

    private lateinit var epoxyController: PostDetailEpoxyController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_post_detail, container, false)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(rootView.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        epoxyController = PostDetailEpoxyController(presenter::onCommentLongClicked)
        rootView.postDetailRecyclerView.setController(epoxyController)
        return rootView
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onStart() {
        super.onStart()
        val selectedPost = arguments?.getParcelable<PostView>(ARG_SELECTED_POST)
            ?: throw IllegalStateException("Use the newInstance method to instantiate this fragment.")
        presenter.start(selectedPost)
    }

    override fun onStop() {
        super.onStop()
        presenter.stop()
    }

    override fun showPost(post: PostView) {
        epoxyController.post = post
        epoxyController.requestModelBuild()
    }

    override fun showComments(comments: List<CommentView>) {
        epoxyController.comments = comments
    }

    companion object {
        private const val ARG_SELECTED_POST = "selected_post"

        fun newInstance(selectedPost: PostView) = PostDetailFragment().apply {
            arguments = bundleOf(ARG_SELECTED_POST to selectedPost)
        }
    }
}
