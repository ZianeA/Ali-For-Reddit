package com.visualeap.aliforreddit.util.fake

import com.visualeap.aliforreddit.data.comment.CommentResponse
import com.visualeap.aliforreddit.data.comment.CommentWebService
import com.visualeap.aliforreddit.domain.comment.Comment
import io.reactivex.Single
import java.io.IOException

class FakeCommentWebService : CommentWebService {
    private val postToComments = mutableMapOf<String, MutableList<CommentResponse.Comment>>()
    private val subredditToPosts = mutableMapOf<String, MutableList<String>>()
    private var error = false

    fun addComments(subreddit: String, comments: List<Comment>) {
        val postId = comments.first().postId

        subredditToPosts.getOrPut(subreddit) { mutableListOf() }
            .add(postId)

        postToComments.getOrPut(postId) { mutableListOf() }
            .addAll(comments.map { it.toResponse(postId) })
    }

    fun addComment(subreddit: String, comment: Comment) = addComments(subreddit, listOf(comment))

    fun deleteAll() {
        postToComments.clear()
        subredditToPosts.clear()
    }

    fun simulateError() {
        error = true
    }

    override fun getCommentsByPost(subreddit: String, postId: String): Single<CommentResponse> {
        if (error) return Single.error(IOException())

        val isPostPartOfSubreddit =
            subredditToPosts.getOrDefault(subreddit, mutableListOf()).contains(postId)
        val comments = postToComments[postId]

        return if (isPostPartOfSubreddit && comments != null) Single.just(CommentResponse(comments))
        else Single.just(CommentResponse(emptyList()))
    }

    private fun Comment.toResponse(postId: String): CommentResponse.Comment {
        return CommentResponse.Comment(
            id,
            authorName,
            text,
            score,
            creationDate,
            depth,
            postId,
            parentId ?: postId,
            replies?.let { reply -> reply.map { it.toResponse(postId) } }
        )
    }
}