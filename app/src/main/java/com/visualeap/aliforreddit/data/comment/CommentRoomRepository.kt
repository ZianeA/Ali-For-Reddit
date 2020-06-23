package com.visualeap.aliforreddit.data.comment

import com.visualeap.aliforreddit.domain.comment.Comment
import com.visualeap.aliforreddit.domain.comment.CommentRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentRoomRepository @Inject constructor(private val commentDao: CommentDao) :
    CommentRepository {
    override fun getCommentsByPost(postId: String): Observable<List<Comment>> {
        return commentDao.getByPostId(postId)
            .map(CommentEntityMapper::mapToDomain)
    }

    override fun addComment(comment: Comment): Completable {
        return commentDao.addAll(CommentEntityMapper.mapToEntity(comment))
    }

    override fun addComments(comments: List<Comment>): Completable {
        return commentDao.addAll(CommentEntityMapper.mapToEntity(comments))
    }

    companion object {
        private const val POST_PREFIX = "t3_"
        private const val SUBREDDIT_PREFIX = "r/"
    }
}