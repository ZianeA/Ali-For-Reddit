package com.visualeap.aliforreddit.domain.repository

import androidx.paging.PagedList
import com.visualeap.aliforreddit.domain.model.Post
import io.reactivex.Observable
import io.reactivex.Single

interface PostRepository {
    fun getPostsBySubreddit(subreddit: String): Observable<PagedList<Post>>
    fun getHomePosts(refresh: Boolean): Single<Listing<Post>>
    fun getPostById(id: String): Single<Post>
    fun getPopularPosts(refresh: Boolean): Single<Listing<Post>>
}
