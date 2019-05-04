package com.visualeap.aliforreddit.presentation.frontPage

import com.visualeap.aliforreddit.domain.repository.PostRepository
import com.visualeap.aliforreddit.core.util.scheduler.SchedulerProvider
import com.visualeap.aliforreddit.core.util.applySchedulers
import io.reactivex.disposables.CompositeDisposable

class FrontPagePresenter(
    private val view: FrontPageView,
    private val repository: PostRepository,
    private val schedulerProvider: SchedulerProvider
) {

    private val disposables = CompositeDisposable()

    fun loadPosts() {
        val disposable = repository.getPosts()
            .applySchedulers(schedulerProvider)
            .subscribe { view.displayPosts(it) }

        disposables.add(disposable)
    }

    //TODO - call this on onPause
    fun stop() {
        disposables.clear()
    }
}