package com.visualeap.aliforreddit.domain.comment

import com.visualeap.aliforreddit.data.comment.CommentResponseMapper
import com.visualeap.aliforreddit.data.comment.CommentWebService
import com.visualeap.aliforreddit.domain.util.Lce
import com.visualeap.aliforreddit.domain.util.toLce
import dagger.Reusable
import io.reactivex.Observable
import javax.inject.Inject

@Reusable
class GetCommentsByPost @Inject constructor(
    private val commentRepository: CommentRepository,
    private val commentService: CommentWebService
) {
    fun execute(postId: String, subreddit: String): Observable<Lce<List<Comment>>> {
        return Observable.merge(loadCache(postId), refreshCache(subreddit, postId))
            .distinctUntilChanged()
    }

    private fun loadCache(postId: String): Observable<Lce<List<Comment>>> {
        return commentRepository.getCommentsByPost(postId)
            // prevent triggering false empty state
            .filter { it.isNotEmpty() }
            .toLce()
    }

    private fun refreshCache(subreddit: String, postId: String): Observable<Lce<List<Comment>>> {
        return commentService.getCommentsByPost(subreddit, postId.removePrefix("t3_"))
            .flatMapObservable { response ->
                commentRepository.addComments(CommentResponseMapper.map(response))
                    // trigger empty state
                    .andThen(Observable.defer {
                        if (response.comments.isEmpty()) {
                            Observable.just(emptyList<Comment>())
                        } else {
                            Observable.never()
                        }
                    })
            }
            .toLce()
    }
}