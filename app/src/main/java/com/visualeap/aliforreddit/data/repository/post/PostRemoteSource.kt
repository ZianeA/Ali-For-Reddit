package com.visualeap.aliforreddit.data.repository.post

import com.visualeap.aliforreddit.domain.model.Post
import io.reactivex.Single

interface PostRemoteSource {
    fun getPosts(subredditName: String): Single<List<Post>>
}