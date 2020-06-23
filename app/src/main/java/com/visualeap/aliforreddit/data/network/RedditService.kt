package com.visualeap.aliforreddit.data.network

import com.visualeap.aliforreddit.data.comment.CommentResponse
import com.visualeap.aliforreddit.data.post.PostResponse
import com.visualeap.aliforreddit.data.redditor.RedditorResponse
import com.visualeap.aliforreddit.data.subreddit.SubredditResponse
import io.reactivex.Single
import retrofit2.http.*

interface RedditService {
    // TODO remove
    @GET("/r/{subreddit}")
    fun getPostsBySubreddit(
        @Path("subreddit") subredditName: String,
        @Query("limit") limit: Int,
        @Query("after") after: String?
    ): Single<PostResponse>

    // TODO remove
    @GET(".")
    fun getHomePosts(@Query("limit") limit: Int, @Query("after") after: String?): Single<PostResponse>

    // TODO remove
    @GET("/api/info")
    fun getSubreddits(@Query("id") subredditIds: String): Single<SubredditResponse>

    @GET("/user/{username}/about")
    fun getRedditor(@Path("username") username: String): Single<RedditorResponse>

    @GET("/api/v1/me")
    fun getCurrentRedditor(): Single<RedditorResponse>

    @GET("/r/{subreddit}/comments/{postId}")
    fun getCommentsByPost(@Path("subreddit") subredditName: String, @Path("postId") postId: String): Single<CommentResponse>
}