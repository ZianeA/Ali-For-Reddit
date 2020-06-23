package com.visualeap.aliforreddit.data.comment

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface CommentWebService {
    @GET("/r/{subreddit}/comments/{postId}")
    fun getCommentsByPost(
        @Path("subreddit") subreddit: String,
        @Path("postId") postId: String
    ): Single<CommentResponse>
}