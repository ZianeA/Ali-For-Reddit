package com.visualeap.aliforreddit.data.subreddit

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface SubredditWebService {
    @GET("/api/info")
    fun getSubreddits(@Query("id") subredditIds: String): Single<SubredditResponse>
}