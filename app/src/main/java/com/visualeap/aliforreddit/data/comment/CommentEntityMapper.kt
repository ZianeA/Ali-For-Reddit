package com.visualeap.aliforreddit.data.comment

import com.visualeap.aliforreddit.domain.util.Mapper
import com.visualeap.aliforreddit.domain.comment.Comment
import dagger.Reusable
import javax.inject.Inject

@Reusable
class CommentEntityMapper @Inject constructor() :
    Mapper<List<@JvmSuppressWildcards CommentEntity>, List<@JvmSuppressWildcards Comment>> {
    override fun map(model: List<CommentEntity>): List<Comment> {
        val commentsByParentId = mutableMapOf<String, MutableList<CommentEntity>>()
        val rootComments = mutableListOf<CommentEntity>()
        model.forEach {
            if (it.parentId == null) {
                rootComments.add(it)
                return@forEach //this acts as continue
            }

            if (commentsByParentId.containsKey(it.parentId)) {
                commentsByParentId[it.parentId]!!.add(it)
            } else {
                commentsByParentId[it.parentId] = mutableListOf(it)
            }
        }

        return createCommentTree(rootComments, commentsByParentId)
    }

    override fun mapReverse(model: List<Comment>): List<CommentEntity> {
        val commentEntities = mutableListOf<CommentEntity>()
        // We are mutating the commentEntities inside the flattenCommentTree to avoid creating redundant lists.
        // This not functional programming. There are probably better ways to do this. This is what I came up with.
        flattenCommentTree(model, commentEntities)
        return commentEntities
    }

    private fun flattenCommentTree(comments: List<Comment>, commentEntities: MutableList<CommentEntity>) {
        comments.forEach {
            commentEntities.add(it.toEntity())
            if (it.replies != null) {
                flattenCommentTree(it.replies, commentEntities)
            }
        }
    }

    private fun createCommentTree(
        commentEntities: List<CommentEntity>,
        commentsByParentId: Map<String, MutableList<CommentEntity>>
    ): List<Comment> {
        val comments = mutableListOf<Comment>()
        commentEntities.forEach { commentEntity ->
            val replies = commentsByParentId[commentEntity.id]

            if (replies != null) {
                val nestedReplies = createCommentTree(replies, commentsByParentId)
                comments.add(commentEntity.toDomain(nestedReplies))
            } else {
                comments.add(commentEntity.toDomain(null))
            }
        }

        return comments
    }

    private fun Comment.toEntity(): CommentEntity {
        return CommentEntity(
            this.id,
            this.authorName,
            this.text,
            this.score,
            this.creationDate,
            this.depth,
            this.postId,
            this.parentId
        )
    }

    private fun CommentEntity.toDomain(replies: List<Comment>?): Comment {
        return Comment(
            this.id,
            this.authorName,
            this.text,
            this.score,
            this.creationDate,
            this.depth,
            this.postId,
            this.parentId,
            replies
        )
    }
}