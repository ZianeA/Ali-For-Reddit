package com.visualeap.aliforreddit.presentation.postDetail

import android.view.View
import com.airbnb.epoxy.AsyncEpoxyController
import com.airbnb.epoxy.SimpleEpoxyModel
import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.presentation.frontPage.PostDto
import com.visualeap.aliforreddit.presentation.frontPage.PostEpoxyModel_
import com.visualeap.aliforreddit.presentation.common.model.CommentDto
import com.visualeap.aliforreddit.presentation.common.util.EpoxyAutoBuild

class PostDetailEpoxyController() : AsyncEpoxyController() {
    var post: PostDto? by EpoxyAutoBuild(this, null)
    var postLoading by EpoxyAutoBuild(this, false)

    var comments: List<CommentDto> by EpoxyAutoBuild(this, emptyList())
    var commentsLoading by EpoxyAutoBuild(this, false)
    var commentsError by EpoxyAutoBuild(this, false)
    var onCommentLongClickListener: ((longClickedComment: CommentDto, allComments: List<CommentDto>) -> Boolean)? =
        null

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

        buildCommentModelsTree(comments)
    }

    private fun buildCommentModelsTree(commentList: List<CommentDto>) {
        commentList.forEach { dto ->
            if (dto.isCollapsed) {
                CollapsedCommentEpoxyModel_()
                    .id(dto.id)
                    .comment(dto)
                    .longClickListener(
                        onCommentLongClickListener?.let { listener ->
                            View.OnLongClickListener { listener(dto, comments) }
                        }
                    )
                    .addTo(this)

                return@forEach //Here return acts as continue
            }

            ExpandedCommentEpoxyModel_()
                .id(dto.id)
                .comment(dto)
                .longClickListener(
                    onCommentLongClickListener?.let { listener ->
                        View.OnLongClickListener { listener(dto, comments) }
                    }
                )
                .addTo(this)

            if (dto.replies != null) {
                buildCommentModelsTree(dto.replies)
            }
        }
    }
}