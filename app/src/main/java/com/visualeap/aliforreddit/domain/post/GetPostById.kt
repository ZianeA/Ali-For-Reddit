package com.visualeap.aliforreddit.domain.post

import com.visualeap.aliforreddit.data.post.Post
import com.visualeap.aliforreddit.data.post.PostResponseMapper
import com.visualeap.aliforreddit.data.post.PostWebService
import com.visualeap.aliforreddit.data.subreddit.Subreddit
import com.visualeap.aliforreddit.data.subreddit.SubredditDao
import com.visualeap.aliforreddit.domain.util.Lce
import com.visualeap.aliforreddit.domain.util.toLce
import dagger.Reusable
import io.reactivex.Observable
import javax.inject.Inject

@Reusable
class GetPostById @Inject constructor(
    private val postRepository: PostRepository,
    private val subredditDao: SubredditDao,
    private val postWebService: PostWebService
) {
    fun execute(postId: String): Observable<Lce<Pair<Subreddit, Post>>> {
        return Observable.merge(loadCache(postId), refreshCache(postId))
            .distinctUntilChanged()
    }

    private fun refreshCache(postId: String): Observable<Lce<Pair<Subreddit, Post>>> {
        return postWebService.getPostsByIds(postId)
            .map { response -> PostResponseMapper.map(response).first() }
            .flatMapObservable { post ->
                postRepository.updatePost(post).toObservable<Pair<Subreddit, Post>>()
            }
            .toLce()
    }

    private fun loadCache(postId: String): Observable<Lce<Pair<Subreddit, Post>>> {
        return subredditDao.getByPost(postId)
            .flatMap { subreddit ->
                postRepository.getPostById(postId).map { post -> subreddit to post }
            }
            .toLce()
    }
}