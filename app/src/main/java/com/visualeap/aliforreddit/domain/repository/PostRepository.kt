package com.visualeap.aliforreddit.domain.repository

import androidx.paging.PagedList
import com.visualeap.aliforreddit.domain.model.Post
import io.reactivex.Observable

interface PostRepository {
    fun getHomePosts(): Observable<PagedList<Post>>
    fun getPostsBySubreddit(subreddit: String): Observable<PagedList<Post>>
}
