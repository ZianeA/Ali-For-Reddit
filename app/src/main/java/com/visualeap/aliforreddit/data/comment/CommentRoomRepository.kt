package com.visualeap.aliforreddit.data.comment

import com.visualeap.aliforreddit.data.network.RedditService
import com.visualeap.aliforreddit.domain.util.Mapper
import com.visualeap.aliforreddit.domain.comment.Comment
import com.visualeap.aliforreddit.domain.comment.CommentRepository
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommentRoomRepository @Inject constructor(
    private val commentDao: CommentDao,
    private val redditService: RedditService,
    private val commentResponseMapper: @JvmSuppressWildcards Mapper<CommentResponse, List<Comment>>,
    private val commentEntityMapper: @JvmSuppressWildcards Mapper<List<CommentEntity>, List<Comment>>
) : CommentRepository {
    override fun getCommentsByPost(subredditName: String, postId: String): Single<List<Comment>> {
        //Always get fresh comments
        //TODO this fails if there's no internet connection even if there are cached comments.
        return redditService.getCommentsByPost(
            subredditName.removePrefix(SUBREDDIT_PREFIX),
            postId.removePrefix(POST_PREFIX)
        )
            .map(commentResponseMapper::map)
            .map(commentEntityMapper::mapReverse)
            .flatMapCompletable { commentDao.addAll(it) }
            .andThen(commentDao.getByPostId(postId))
            .map(commentEntityMapper::map)
    }

    companion object {
        private const val POST_PREFIX = "t3_"
        private const val SUBREDDIT_PREFIX = "r/"
    }
}