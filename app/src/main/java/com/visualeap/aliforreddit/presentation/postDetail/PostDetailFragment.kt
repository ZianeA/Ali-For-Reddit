package com.visualeap.aliforreddit.presentation.postDetail


import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.presentation.common.view.drawer.DrawerController
import com.visualeap.aliforreddit.presentation.common.model.CommentView
import com.visualeap.aliforreddit.presentation.common.model.PostView
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_post_detail.view.*
import javax.inject.Inject

class PostDetailFragment : Fragment(), PostDetailView {
    @Inject
    lateinit var presenter: PostDetailPresenter

    @Inject
    lateinit var drawerController: DrawerController

    private lateinit var epoxyController: PostDetailEpoxyController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        drawerController.lockClosed()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_post_detail, container, false)

        (activity as AppCompatActivity).apply {
            rootView.toolbar.setBackgroundColor(Color.parseColor(selectedPost.subreddit.color))
            setSupportActionBar(rootView.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        epoxyController = PostDetailEpoxyController(presenter::onCommentLongClicked)
        rootView.postDetailRecyclerView.apply {
            setController(epoxyController)
            addItemDecoration(CommentItemDecoration(resources.getDimension(R.dimen.comment_spacing).toInt()))
        }
        return rootView
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onStart() {
        super.onStart()
        presenter.start(selectedPost)
    }

    override fun onStop() {
        super.onStop()
        presenter.stop()
    }

    override fun showPost(post: PostView) {
        // TODO
//        epoxyController.post = post
        epoxyController.requestModelBuild()
    }

    override fun showComments(comments: List<CommentView>) {
        epoxyController.comments = comments
    }

    private val selectedPost: PostView
        get() = arguments?.getParcelable<PostView>(ARG_SELECTED_POST)
            ?: throw IllegalStateException("Use the newInstance method to instantiate this fragment.")

    companion object {
        private const val ARG_SELECTED_POST = "selected_post"

        fun newInstance(/*selectedPost: PostView*/) = PostDetailFragment()/*.apply {
            arguments = bundleOf(ARG_SELECTED_POST to selectedPost)
        }*/
    }
}
