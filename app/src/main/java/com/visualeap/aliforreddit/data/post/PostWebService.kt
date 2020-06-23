package com.visualeap.aliforreddit.data.post

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PostWebService {
    @GET("/r/{subreddit}")
    fun getPostsBySubreddit(
        @Path("subreddit") subredditName: String,
        @Query("limit") limit: Int,
        @Query("after") after: String?
    ): Single<PostResponse>

    @GET(".")
    fun getHomePosts(@Query("limit") limit: Int, @Query("after") after: String?): Single<PostResponse>
}