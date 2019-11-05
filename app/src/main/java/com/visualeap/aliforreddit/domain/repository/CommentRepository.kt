package com.visualeap.aliforreddit.domain.repository

import com.visualeap.aliforreddit.domain.model.Comment
import io.reactivex.Single

interface CommentRepository {
    fun getCommentsByPost(
        subredditName: String,
        postId: String,
        onNext: (t: NetworkState) -> Unit,
        onError: (t: Throwable) -> Unit
    ): Single<List<Comment>>
}
