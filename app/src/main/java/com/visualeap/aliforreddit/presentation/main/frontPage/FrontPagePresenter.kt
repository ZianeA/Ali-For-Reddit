package com.visualeap.aliforreddit.presentation.main.frontPage

import com.visualeap.aliforreddit.presentation.di.FragmentScope
import com.visualeap.aliforreddit.domain.repository.PostRepository
import com.visualeap.aliforreddit.domain.util.scheduler.SchedulerProvider
import com.visualeap.aliforreddit.domain.util.applySchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@FragmentScope
class FrontPagePresenter @Inject constructor(
    private val view: FrontPageView,
    private val repository: PostRepository,
    private val schedulerProvider: SchedulerProvider
) {
    private val disposables = CompositeDisposable()

    fun start() {
        val disposable = repository.getHomePosts()
            .applySchedulers(schedulerProvider)
            .subscribe({ view.displayPosts(it) }, { println("OnError was called: ${it.message}")/*TODO implement on error*/})

        disposables.add(disposable)
    }

    fun stop() {
        disposables.clear()
    }
}