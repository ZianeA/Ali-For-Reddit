package com.visualeap.aliforreddit.domain.repository

import com.visualeap.aliforreddit.domain.model.Post
import io.reactivex.Single

interface PostRepository {
    fun getPosts(): Single<List<Post>>
}
