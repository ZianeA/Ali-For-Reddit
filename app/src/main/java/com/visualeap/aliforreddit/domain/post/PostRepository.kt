package com.visualeap.aliforreddit.domain.post

import com.visualeap.aliforreddit.domain.feed.SortType
import io.reactivex.*

interface PostRepository {
    fun getPostById(id: String): Observable<Post>

    fun getPostsByFeed(
        feed: String,
        sortType: SortType,
        offset: Int,
        limit: Int
    ): Flowable<List<Post>>

    fun countPostsByFeed(feed: String, sortType: SortType): Single<Int>
    fun addPost(post: Post, feed: String, sortType: SortType): Completable
    fun addPosts(posts: List<Post>, feed: String, sortType: SortType): Completable
    fun updatePost(post: Post): Completable
}
