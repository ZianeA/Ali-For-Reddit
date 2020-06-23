package com.visualeap.aliforreddit.domain.comment

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

interface CommentRepository {
    fun getCommentsByPost(postId: String): Observable<List<Comment>>
    fun addComment(comment: Comment): Completable
    fun addComments(comments: List<Comment>): Completable
}
