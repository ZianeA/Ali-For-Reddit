package com.visualeap.aliforreddit.data.repository

import android.util.Log
import com.visualeap.aliforreddit.domain.entity.Post
import com.visualeap.aliforreddit.domain.repository.PostRepository
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostDataRepository @Inject constructor() : PostRepository {

    override fun getPosts(): Observable<List<Post>> {
        Log.i(PostDataRepository::class.java.simpleName, Thread.currentThread().name)

        return Observable.just(emptyList())
    }
}