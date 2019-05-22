package com.visualeap.aliforreddit.domain.repository

import com.visualeap.aliforreddit.domain.entity.Post
import io.reactivex.Observable
import io.reactivex.Single

interface PostRepository {
    fun getPosts(): Single<List<Post>>
}
