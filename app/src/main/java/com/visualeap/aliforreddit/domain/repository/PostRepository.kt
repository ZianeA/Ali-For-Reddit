package com.visualeap.aliforreddit.domain.repository

import com.visualeap.aliforreddit.domain.model.Listing
import com.visualeap.aliforreddit.domain.model.Post
import com.visualeap.aliforreddit.domain.model.Subreddit
import com.visualeap.aliforreddit.domain.model.feed.SortType
import com.visualeap.aliforreddit.domain.usecase.FetchFeedPosts
import io.reactivex.*

interface PostRepository {
    fun getPostsByFeed(
        feed: String,
        sortType: SortType,
        offset: Int,
        limit: Int
    ): Flowable<List<Post>>

    fun countPostsByFeed(feed: String, sortType: SortType): Single<Int>

    fun addPosts(posts: List<Post>, feed: String, sortType: SortType): Completable
}
