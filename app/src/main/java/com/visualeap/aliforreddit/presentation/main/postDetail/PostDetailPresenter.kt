package com.visualeap.aliforreddit.presentation.main.postDetail

import com.visualeap.aliforreddit.domain.model.Post
import com.visualeap.aliforreddit.domain.repository.CommentRepository
import com.visualeap.aliforreddit.domain.repository.PostRepository
import com.visualeap.aliforreddit.domain.util.applySchedulers
import com.visualeap.aliforreddit.domain.util.scheduler.SchedulerProvider
import com.visualeap.aliforreddit.presentation.di.FragmentScope
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@FragmentScope
class PostDetailPresenter @Inject constructor(
    private val view: PostDetailView,
    private val commentRepository: CommentRepository,
    private val schedulerProvider: SchedulerProvider
) {
    private val disposables = CompositeDisposable()

    fun start(post: Post) {
        view.showPost(post)

        val disposable = commentRepository.getCommentsByPost(post.subreddit.name, post.id,
            { print(it.status) },
            { it.message })
            .applySchedulers(schedulerProvider)
            .subscribe({ view.showComments(it) }, { /*onError*/ })

        disposables.add(disposable)
    }

    fun stop() {
        disposables.clear()
    }
}
