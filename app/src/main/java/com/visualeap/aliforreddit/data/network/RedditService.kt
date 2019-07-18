package com.visualeap.aliforreddit.data.network

import com.visualeap.aliforreddit.domain.model.Post
import com.visualeap.aliforreddit.domain.model.User
import io.reactivex.Single
import retrofit2.http.*

interface RedditService {
    //TODO fix or remove
    @GET("/r/androiddev/about")
    fun getPosts() : Single<List<Post>>

    @GET("/api/v1/me")
    fun getCurrentUser() : Single<User>
}