package com.visualeap.aliforreddit.presentation.main.frontPage

import androidx.paging.PagedList
import com.visualeap.aliforreddit.domain.model.Post
import com.visualeap.aliforreddit.presentation.di.FragmentScope
import com.visualeap.aliforreddit.domain.repository.PostRepository
import com.visualeap.aliforreddit.domain.util.scheduler.SchedulerProvider
import com.visualeap.aliforreddit.domain.util.applySchedulers
import io.reactivex.Observable
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
        val listingDisposable = repository.getHomePosts(false)
            .applySchedulers(schedulerProvider)
            .subscribe({ postListing ->
                val pagedListDisposable = postListing.pagedList
                    .applySchedulers(schedulerProvider)
                    .subscribe(
                        { view.displayPosts(it) },
                        { println("OnError was called: ${it.message}")/*TODO implement on error*/ })

                val networkDisposable = postListing.networkState
                    .subscribe { /*TODO handle network state*/ }

                disposables.addAll(pagedListDisposable, networkDisposable)
            },
                { /*onError*/ }
            )

        disposables.add(listingDisposable)
    }

    fun stop() {
        disposables.clear()
    }
}