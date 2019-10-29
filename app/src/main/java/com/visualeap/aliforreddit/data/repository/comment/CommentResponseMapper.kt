package com.visualeap.aliforreddit.data.repository.comment

import com.visualeap.aliforreddit.data.repository.Mapper
import com.visualeap.aliforreddit.domain.model.Comment
import dagger.Reusable
import javax.inject.Inject

@Reusable
class CommentResponseMapper @Inject constructor() :
    Mapper<CommentResponse, List<@JvmSuppressWildcards Comment>> {
    override fun map(model: CommentResponse): List<Comment> {
        return model.comments.map {
            Comment(
                it.id,
                it.authorName,
                it.text,
                it.score,
                it.creationDate,
                it.depth,
                it.postId,
                if (it.parentId != it.postId) it.parentId else null,
                if (it.replies != null) map(CommentResponse(it.replies)) else null
            )
        }
    }

    override fun mapReverse(model: List<Comment>): CommentResponse {
        TODO("not needed") //To change body of created functions use File | Settings | File Templates.
    }
}