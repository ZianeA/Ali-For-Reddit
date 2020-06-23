package com.visualeap.aliforreddit.data.network

import com.visualeap.aliforreddit.data.comment.CommentResponse
import com.visualeap.aliforreddit.data.post.PostResponse
import com.visualeap.aliforreddit.data.redditor.RedditorResponse
import com.visualeap.aliforreddit.data.subreddit.SubredditResponse
import io.reactivex.Single
import retrofit2.http.*

interface RedditService {
    @GET("/user/{username}/about")
    fun getRedditor(@Path("username") username: String): Single<RedditorResponse>

    @GET("/api/v1/me")
    fun getCurrentRedditor(): Single<RedditorResponse>
}