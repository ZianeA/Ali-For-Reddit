package com.visualeap.aliforreddit.presentation.common.formatter

import com.visualeap.aliforreddit.domain.comment.Comment
import com.visualeap.aliforreddit.presentation.common.model.CommentDto

object CommentFormatter {
    fun format(comments: List<Comment>): List<CommentDto> {
        return reverseMapCommentTree(comments, true)
    }

    private fun reverseMapCommentTree(
        comments: List<Comment>,
        isHeadTheDeepestNode: Boolean
    ): List<CommentDto> {
        val commentViews = mutableListOf<CommentDto>()
        comments.forEachIndexed { i, it ->
            val isDeepestNode = it.depth == 0 || (isHeadTheDeepestNode && i == comments.lastIndex)

            if (it.replies != null) {
                val nestedReplies = reverseMapCommentTree(it.replies, isDeepestNode)
                commentViews.add(it.toView(nestedReplies, false))
            } else {
                commentViews.add(it.toView(null, isDeepestNode))
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