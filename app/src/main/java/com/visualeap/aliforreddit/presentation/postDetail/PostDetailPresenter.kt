package com.visualeap.aliforreddit.presentation.postDetail

import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.domain.comment.GetCommentsByPost
import com.visualeap.aliforreddit.domain.post.GetPostById
import com.visualeap.aliforreddit.domain.util.Lce
import com.visualeap.aliforreddit.domain.util.autoReplay
import com.visualeap.aliforreddit.domain.util.scheduler.SchedulerProvider
import com.visualeap.aliforreddit.presentation.common.di.FragmentScope
import com.visualeap.aliforreddit.presentation.common.formatter.CommentFormatter
import com.visualeap.aliforreddit.presentation.common.formatter.PostFormatter
import com.visualeap.aliforreddit.presentation.common.model.CommentDto
import com.visualeap.aliforreddit.presentation.common.util.ResourceProvider
import com.visualeap.aliforreddit.presentation.postDetail.PostDetailEvent.*
import com.visualeap.aliforreddit.presentation.postDetail.PostDetailResult.*
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

@FragmentScope
class PostDetailPresenter @Inject constructor(
    private val launcher: PostDetailLauncher,
    @PostId private val postId: String,
    @Subreddit private val subreddit: String,
    private val getPostById: GetPostById,
    private val getCommentsByPost: GetCommentsByPost,
    private val resourceProvider: ResourceProvider,
    private val schedulerProvider: SchedulerProvider
) {
    private val events: PublishSubject<PostDetailEvent> = PublishSubject.create()

    val viewState: Observable<PostDetailViewState> by lazy {
        events.publish { event ->
            Observable.merge(
                event.ofType(ScreenLoadEvent::class.java).take(1).handleScreenLoad(),
                event.ofType(CollapseCommentEvent::class.java).handleCollapseComment()
            )
        }
            .resultToViewState()
            .autoReplay()
    }

    fun passEvents(event: PostDetailEvent) = events.onNext(event)

    private fun Observable<ScreenLoadEvent>.handleScreenLoad(): Observable<ScreenLoadResult> {
        return publish { screenLoadEvent ->
            Observable.merge(
                screenLoadEvent.flatMap { loadPost() },
                screenLoadEvent.flatMap { loadComments() })
        }
    }

    private fun loadPost(): Observable<ScreenLoadResult> {
        return getPostById.execute(postId)
            .subscribeOn(schedulerProvider.io)
            .map { lce ->
                when (lce) {
                    is Lce.Loading -> ScreenLoadResult.PostLoading
                    is Lce.Error -> ScreenLoadResult.PostError(resourceProvider.getString(R.string.error_server))
                    is Lce.Content -> {
                        val (subreddit, post) = lce.data.first to lce.data.second
                        ScreenLoadResult.PostContent(PostFormatter.formatPost(subreddit, post))
                    }
                }
            }
    }

    private fun loadComments(): Observable<ScreenLoadResult> {
        return getCommentsByPost.execute(postId, subreddit)
            .subscribeOn(schedulerProvider.io)
            .map { lce ->
                when (lce) {
                    is Lce.Loading -> ScreenLoadResult.CommentsLoading
                    is Lce.Content ->
                        ScreenLoadResult.CommentsContent(CommentFormatter.format(lce.data))
                    is Lce.Error -> ScreenLoadResult.CommentsError(resourceProvider.getString(R.string.error_server))
                }
            }
    }

    private fun Observable<CollapseCommentEvent>.handleCollapseComment(): Observable<CollapseCommentResult> {
        return Observable.just(CollapseCommentResult)
    }

    private fun Observable<PostDetailResult>.resultToViewState(): Observable<PostDetailViewState> {
        return scan(PostDetailViewState()) { vs, result ->
            when (result) {
                is ScreenLoadResult -> when (result) {
                    is ScreenLoadResult.PostContent -> vs.copy(post = result.post)
                    is ScreenLoadResult.PostError -> vs.copy(postError = result.error)
                    ScreenLoadResult.PostLoading -> vs.copy(postLoading = true)

                    is ScreenLoadResult.CommentsContent -> vs.copy(comments = result.comments)
                    is ScreenLoadResult.CommentsError -> vs.copy(commentsError = result.error)
                    ScreenLoadResult.CommentsLoading -> vs.copy(commentsLoading = true)
                }
                is CollapseCommentResult -> vs.copy()
            }
        }
            .distinctUntilChanged()
    }

    /*fun onCommentLongClicked(clickedComment: CommentDto, allComments: List<CommentDto>): Boolean {
        val comments = collapseOrExpandComment(allComments, clickedComment.id)
        launcher.showComments(comments)
        return true
    }*/

    private fun getComments(): Observable<PostDetailViewState> {
        /*return getCommentsByPost.execute(postId, subreddit)
            .map<PostDetailViewState> { CommentSuccess(CommentFormatter.format(it), null) }
            .startWith(Loading)*/
        TODO("Not implemented")
    }

    private fun getPost(): Observable<PostDetailViewState> {
        /*return getPostById.execute(postId)
            .map<PostDetailViewState> { (subreddit, post) ->
                PostSuccess(PostFormatter.formatPost(subreddit, post), null)
            }
            .startWith(Loading)*/
        TODO("Not implemented")
    }

    private fun collapseOrExpandComment(
        comments: List<CommentDto>,
        commentId: String
    ): List<CommentDto> {
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
}
