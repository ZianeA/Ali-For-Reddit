package com.visualeap.aliforreddit.domain.comment

import io.reactivex.Single

interface CommentRepository {
    fun getCommentsByPost(subredditName: String, postId: String): Single<List<Comment>>
}
