package com.visualeap.aliforreddit.data.network

import com.visualeap.aliforreddit.data.repository.post.PostResponse
import io.reactivex.Single
import retrofit2.http.*

interface RedditService {
    @GET("/r/{subreddit}")
    fun getPosts(@Path("subreddit") subredditName: String): Single<PostResponse>

    @GET(".")
    fun getHomePosts(@Query("limit") limit: Int, @Query("after") after: String?): Single<PostResponse>

    /*@GET("/r/{subreddit}/about")
    fun getSubreddit(@Path("subreddit") subredditName: String): Single<Subreddit>*/
}