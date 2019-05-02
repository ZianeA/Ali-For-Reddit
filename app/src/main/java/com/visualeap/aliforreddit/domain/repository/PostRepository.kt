package com.visualeap.aliforreddit.domain.repository

import com.visualeap.aliforreddit.domain.entity.Post
import io.reactivex.Observable

interface PostRepository {

    fun getPosts(): Observable<List<Post>>
}
