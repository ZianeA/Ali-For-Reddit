package com.visualeap.aliforreddit.data.comment

import com.visualeap.aliforreddit.domain.comment.Comment

object CommentResponseMapper {
    fun map(response: CommentResponse): List<Comment> {
        return response.comments.map {
            Comment(
                it.id,
                it.authorName,
                it.text,
                it.score,
                it.creationDate,
                it.depth,
                it.postId,
                if (it.parentId == it.postId) null else it.parentId,
                it.replies?.let { replies -> map(CommentResponse(replies)) }
            )
        }
    }
}