package com.visualeap.aliforreddit.data.post

import com.visualeap.aliforreddit.data.subreddit.SubredditResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PostWebService {
    @GET("/api/info")
    fun getPostsByIds(@Query("id") postId: String): Single<PostResponse>

    @GET("/r/{subreddit}")
    fun getPostsBySubreddit(
        @Path("subreddit") subredditName: String,
        @Query("limit") limit: Int,
        @Query("after") after: String?
    ): Single<PostResponse>

    @GET(".")
    fun getHomePosts(@Query("limit") limit: Int, @Query("after") after: String?): Single<PostResponse>
}