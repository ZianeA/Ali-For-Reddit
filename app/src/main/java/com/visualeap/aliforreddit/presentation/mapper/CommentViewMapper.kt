package com.visualeap.aliforreddit.presentation.mapper

import com.visualeap.aliforreddit.domain.model.Comment
import com.visualeap.aliforreddit.domain.util.Mapper
import com.visualeap.aliforreddit.presentation.model.CommentView
import com.visualeap.aliforreddit.presentation.util.formatCount
import com.visualeap.aliforreddit.presentation.util.formatTimestamp
import dagger.Reusable
import javax.inject.Inject

@Reusable
class CommentViewMapper @Inject constructor() :
    Mapper<List<@JvmSuppressWildcards CommentView>, List<@JvmSuppressWildcards Comment>> {
    override fun map(model: List<CommentView>): List<Comment> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun mapReverse(model: List<Comment>): List<CommentView> {
        return reverseMapCommentTree(model)
    }

    private fun reverseMapCommentTree(
        comments: List<Comment>,
        isParentLastReply: Boolean? = null
    ): List<CommentView> {
        val commentViews = mutableListOf<CommentView>()
        comments.forEachIndexed { i, it ->
            var isLastReply = true
            if (isParentLastReply != null && (!isParentLastReply || i != comments.lastIndex)) {
                isLastReply = false
            }
            if (it.replies != null) {
                val nestedReplies = reverseMapCommentTree(it.replies, isLastReply)
                commentViews.add(it.toView(nestedReplies, false))
            } else {
                commentViews.add(it.toView(null, isLastReply))
            }
        }

        return commentViews
    }

    private fun Comment.toView(replies: List<CommentView>?, isLastReply: Boolean): CommentView {
        return CommentView(
            id,
            authorName,
            text,
            formatCount(score),
            formatTimestamp(creationDate),
            depth,
            postId,
            parentId,
            replies,
            false,
            isLastReply
        )
    }
}