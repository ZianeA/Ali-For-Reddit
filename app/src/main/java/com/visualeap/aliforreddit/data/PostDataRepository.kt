package com.visualeap.aliforreddit.data

import android.util.Log
import com.visualeap.aliforreddit.domain.entity.Post
import com.visualeap.aliforreddit.domain.repository.PostRepository
import io.reactivex.Observable

class PostDataRepository : PostRepository {

    override fun getPosts(): Observable<List<Post>> {
        Log.i(PostDataRepository::class.java.simpleName, Thread.currentThread().name)

        return Observable.just(emptyList())
    }
}