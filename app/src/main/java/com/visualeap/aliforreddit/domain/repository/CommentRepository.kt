package com.visualeap.aliforreddit.domain.repository

import androidx.paging.PagedList
import com.visualeap.aliforreddit.data.repository.comment.CommentResponse
import com.visualeap.aliforreddit.domain.model.Comment
import com.visualeap.aliforreddit.domain.util.NetworkState
import io.reactivex.Observable
import io.reactivex.Single

interface CommentRepository {
    fun getCommentsByPost(
        subredditName: String,
        postId: String,
        onNext: (t: NetworkState) -> Unit,
        onError: (t: Throwable) -> Unit
    ): Single<List<Comment>>
}
