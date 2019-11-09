package com.visualeap.aliforreddit.presentation.main.postDetail

import android.view.View
import com.airbnb.epoxy.AsyncEpoxyController
import com.visualeap.aliforreddit.domain.model.Comment
import com.visualeap.aliforreddit.presentation.main.frontPage.PostEpoxyModel_
import com.visualeap.aliforreddit.presentation.model.CommentView
import com.visualeap.aliforreddit.presentation.model.PostView

class PostDetailEpoxyController(private val onCommentLongClickListener: ((longClickedComment: CommentView, allComments: List<CommentView>) -> Boolean)) :
    AsyncEpoxyController() {
    var comments: List<CommentView> = emptyList()
        set(value) {
            field = value
            requestModelBuild()
        }

    lateinit var post: PostView

    override fun buildModels() {
        PostEpoxyModel_()
            .id(post.id)
            .post(post)
            .listener(View.OnClickListener { }) //TODO refactor
            .maxLines(Int.MAX_VALUE)
            .addTo(this)

        buildCommentModelsTree(comments)
    }

    private fun buildCommentModelsTree(commentList: List<CommentView>) {
        commentList.forEach {
            if (it.isCollapsed) {
                CollapsedCommentEpoxyModel_()
                    .id(it.id)
                    .comment(it)
                    .longClickListener { _ -> onCommentLongClickListener.invoke(it, comments) }
                    .addTo(this)

                return@forEach //Here return acts as continue
            }

            ExpandedCommentEpoxyModel_()
                .id(it.id)
                .comment(it)
                .longClickListener { _ -> onCommentLongClickListener.invoke(it, comments) }
                .addTo(this)

            if (it.replies != null) {
                buildCommentModelsTree(it.replies)
            }
        }
    }
}