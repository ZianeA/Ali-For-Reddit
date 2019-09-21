package com.visualeap.aliforreddit.data.repository.post

import com.visualeap.aliforreddit.data.network.RedditService
import com.visualeap.aliforreddit.domain.model.Post
import com.visualeap.aliforreddit.domain.repository.PostRepository
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostDataRepository @Inject constructor(private val postRemoteSource: PostRemoteSource) :
    PostRepository {
    override fun getPosts(): Single<List<Post>> {
        return postRemoteSource.getPosts("androiddev")
    }
}