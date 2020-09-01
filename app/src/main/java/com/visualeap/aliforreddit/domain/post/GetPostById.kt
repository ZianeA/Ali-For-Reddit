package com.visualeap.aliforreddit.domain.post

import com.visualeap.aliforreddit.data.post.PostResponseMapper
import com.visualeap.aliforreddit.data.post.PostWebService
import com.visualeap.aliforreddit.domain.subreddit.Subreddit
import com.visualeap.aliforreddit.domain.subreddit.SubredditRepository
import com.visualeap.aliforreddit.domain.util.Lce
import com.visualeap.aliforreddit.domain.util.toLce
import dagger.Reusable
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

@Reusable
class GetPostById @Inject constructor(
    private val postRepository: PostRepository,
    private val subredditRepository: SubredditRepository,
    private val postWebService: PostWebService
) {
    fun execute(postId: String): Observable<Lce<Pair<Subreddit, Post>>> {
        return refreshCache(postId)
            .andThen(loadFromCache(postId).toLce())
            .onErrorResumeNext { t: Throwable ->
                loadFromCache(postId)
                    .skip(1)
                    .toLce()
                    .startWith(Lce.Error(t))
            }
            .startWith(
                loadFromCache(postId)
                    .take(1)
                    .toLce()
                    .startWith(Lce.Loading())
            )
            .distinctUntilChanged()
    }

    private fun refreshCache(postId: String): Completable {
        return postWebService.getPostsByIds(postId)
            .map { response -> PostResponseMapper.map(response).first() }
            .flatMapCompletable { post -> postRepository.updatePost(post) }
    }

    private fun loadFromCache(postId: String): Observable<Pair<Subreddit, Post>> {
        return subredditRepository.getSubredditByPost(postId)
            .flatMap { subreddit ->
                postRepository.getPostById(postId)
                    .map { post -> subreddit to post }
            }
    }
}