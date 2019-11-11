package com.visualeap.aliforreddit.presentation.main.postDetail

import com.visualeap.aliforreddit.domain.model.Comment
import com.visualeap.aliforreddit.domain.repository.CommentRepository
import com.visualeap.aliforreddit.domain.util.Mapper
import com.visualeap.aliforreddit.domain.util.applySchedulers
import com.visualeap.aliforreddit.domain.util.scheduler.SchedulerProvider
import com.visualeap.aliforreddit.presentation.di.FragmentScope
import com.visualeap.aliforreddit.presentation.model.CommentView
import com.visualeap.aliforreddit.presentation.model.PostView
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@FragmentScope
class PostDetailPresenter @Inject constructor(
    private val view: PostDetailView,
    private val commentRepository: CommentRepository,
    private val schedulerProvider: SchedulerProvider,
    private val commentViewMapper: @JvmSuppressWildcards Mapper<List<CommentView>, List<Comment>>
) {
    private val disposables = CompositeDisposable()

    fun start(post: PostView) {
        view.showPost(post)

        val disposable = commentRepository.getCommentsByPost(post.subreddit.name, post.id,
            { print(it.status) }, { it.message })
            .map(commentViewMapper::mapReverse)
            .applySchedulers(schedulerProvider)
            .subscribe({ view.showComments(it) }, { /*onError*/ })

        disposables.add(disposable)
    }

    fun onCommentLongClicked(clickedComment: CommentView, allComments: List<CommentView>): Boolean {
        val collapsedOrExpandedComments = collapseOrExpandComment(allComments, clickedComment.id)
        view.showComments(collapsedOrExpandedComments)
        return true
    }

    private fun collapseOrExpandComment(comments: List<CommentView>, commentId: String): List<CommentView> {
        var foundCollapsedOrExpandedComment = false
        return comments.map {
            if (it.id == commentId) {
                foundCollapsedOrExpandedComment = true
                it.copy(isCollapsed = !it.isCollapsed)
            } else if (!foundCollapsedOrExpandedComment && it.replies != null) {
                val replies = collapseOrExpandComment(it.replies, commentId)
                it.copy(replies = replies)
            } else it
        }
    }

    fun stop() {
        disposables.clear()
    }
}
