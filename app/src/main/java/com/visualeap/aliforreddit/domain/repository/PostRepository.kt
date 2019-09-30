package com.visualeap.aliforreddit.domain.repository

import androidx.paging.PagedList
import com.visualeap.aliforreddit.domain.model.Post
import com.visualeap.aliforreddit.domain.util.NetworkState
import io.reactivex.Observable
import io.reactivex.Observer
import java.util.function.Consumer

interface PostRepository {
    fun getPostsBySubreddit(subreddit: String): Observable<PagedList<Post>>
    fun getHomePosts(
        onNext: (t: NetworkState) -> Unit,
        onError: (t: Throwable) -> Unit
    ): Observable<PagedList<Post>>
}
