package com.visualeap.aliforreddit.domain.comment

import com.visualeap.aliforreddit.data.comment.CommentResponseMapper
import com.visualeap.aliforreddit.data.comment.CommentWebService
import com.visualeap.aliforreddit.domain.util.Lce
import com.visualeap.aliforreddit.domain.util.toLce
import dagger.Reusable
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

@Reusable
class GetCommentsByPost @Inject constructor(
    private val commentRepository: CommentRepository,
    private val commentService: CommentWebService
) {
    fun execute(postId: String, subreddit: String): Observable<Lce<List<Comment>>> {
        return refreshCache(subreddit, postId)
            .andThen(commentRepository.getCommentsByPost(postId).toLce())
            .onErrorResumeNext { t: Throwable ->
                commentRepository.getCommentsByPost(postId)
                    .skip(1)
                    .toLce()
                    .startWith(Lce.Error(t))
            }
            .startWith(
                commentRepository.getCommentsByPost(postId)
                    .take(1)
                    .filter { it.isNotEmpty() }
                    .toLce()
                    .startWith(Lce.Loading())
            )
            .distinctUntilChanged()
    }

    private fun refreshCache(subreddit: String, postId: String): Completable {
        return commentService.getCommentsByPost(subreddit, postId.removePrefix("t3_"))
            .flatMapCompletable { response ->
                commentRepository.addComments(CommentResponseMapper.map(response))
            }
    }
}