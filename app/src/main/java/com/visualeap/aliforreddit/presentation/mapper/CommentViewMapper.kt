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

    private fun reverseMapCommentTree(comments: List<Comment>): List<CommentView> {
        val commentViews = mutableListOf<CommentView>()
        comments.forEach {
            if (it.replies != null) {
                val nestedReplies = reverseMapCommentTree(it.replies)
                commentViews.add(it.toView(nestedReplies))
            } else {
                commentViews.add(it.toView(null))
            }
        }

        return commentViews
    }

    private fun Comment.toView(replies: List<CommentView>?): CommentView {
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
            false
        )
    }
}