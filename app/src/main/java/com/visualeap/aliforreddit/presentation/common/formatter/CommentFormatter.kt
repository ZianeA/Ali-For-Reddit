package com.visualeap.aliforreddit.presentation.common.formatter

import com.visualeap.aliforreddit.domain.comment.Comment
import com.visualeap.aliforreddit.presentation.common.model.CommentDto

object CommentFormatter {
    fun format(comments: List<Comment>): List<CommentDto> {
        return reverseMapCommentTree(comments)
    }

    private fun reverseMapCommentTree(
        comments: List<Comment>,
        isParentLastReply: Boolean? = null
    ): List<CommentDto> {
        val commentViews = mutableListOf<CommentDto>()
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

    private fun Comment.toView(replies: List<CommentDto>?, isLastReply: Boolean): CommentDto {
        return CommentDto(
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