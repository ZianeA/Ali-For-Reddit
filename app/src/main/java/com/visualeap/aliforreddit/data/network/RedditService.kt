package com.visualeap.aliforreddit.data.network

import com.visualeap.aliforreddit.data.network.post.PostResponse
import com.visualeap.aliforreddit.domain.model.Post
import io.reactivex.Single
import retrofit2.http.*

interface RedditService {
    @GET("/r/{subreddit}")
    fun getPosts(@Path("subreddit") subredditName: String): Single<PostResponse>

    /*@GET("/r/{subreddit}/about")
    fun getSubreddit(@Path("subreddit") subredditName: String): Single<Subreddit>*/
}