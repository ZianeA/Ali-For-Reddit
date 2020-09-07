package com.visualeap.aliforreddit.presentation.postDetail

import android.view.View
import com.airbnb.epoxy.AsyncEpoxyController
import com.airbnb.epoxy.SimpleEpoxyModel
import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.domain.comment.Comment
import com.visualeap.aliforreddit.presentation.frontPage.PostDto
import com.visualeap.aliforreddit.presentation.frontPage.PostEpoxyModel_
import com.visualeap.aliforreddit.presentation.common.util.EpoxyAutoBuild

class PostDetailEpoxyController() : AsyncEpoxyController() {
    var post: PostDto? by EpoxyAutoBuild(this, null)
    var postLoading by EpoxyAutoBuild(this, false)
    var comments: List<Comment> by EpoxyAutoBuild(this, emptyList())
    var commentsLoading by EpoxyAutoBuild(this, false)
    var commentsError by EpoxyAutoBuild(this, false)

    private var collapsedComments = mutableSetOf<String>()

    override fun buildModels() {
        post?.let {
            PostEpoxyModel_()
                .id(it.id)
                .post(it)
                .clickListener(View.OnClickListener { }) //TODO refactor
                .maxLines(Int.MAX_VALUE)
                .addTo(this)
        }

        SimpleEpoxyModel(R.layout.item_loading)
            .id("Loading Post")
            .addIf(postLoading, this)

        SimpleEpoxyModel(R.layout.item_loading)
            .id("Loading Comments")
            .addIf(commentsLoading, this)

        SimpleEpoxyModel(R.layout.item_error_comment)
            .id("Error Comment")
            .addIf(commentsError, this)

        comments.forEach { c ->
            val models = createCommentModels(listOf(c))
            models.last().isLastReplay = true
            add(models)
        }
    }

    private fun createCommentModels(commentTree: List<Comment>): List<CommentEpoxyModel<out CommentHolder>> {
        val models = mutableListOf<CommentEpoxyModel<out CommentHolder>>()
        commentTree.forEach { c ->
            if (c.isCollapsed()) {
                models += CollapsedCommentEpoxyModel_()
                    .id(c.id)
                    .comment(c)
                    .longClickListener { _ -> onCommentLongClicked(c.id) }

                return@forEach //continue
            }

            models += ExpandedCommentEpoxyModel_()
                .id(c.id)
                .comment(c)
                .longClickListener { _ -> onCommentLongClicked(c.id) }

            if (c.replies != null) {
                models += createCommentModels(c.replies)
            }
        }

        return models
    }

    private fun Comment.isCollapsed() = collapsedComments.contains(id)

    private fun onCommentLongClicked(commentId: String): Boolean {
        if (collapsedComments.contains(commentId)) {
            collapsedComments.remove(commentId)
        } else {
            collapsedComments.add(commentId)
        }
        requestModelBuild()

        return true
    }
}