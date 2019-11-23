package com.visualeap.aliforreddit.presentation.main.frontPage

import androidx.paging.PagedList
import com.visualeap.aliforreddit.data.repository.feed.DefaultFeed
import com.visualeap.aliforreddit.domain.model.Post
import com.visualeap.aliforreddit.presentation.di.FragmentScope
import com.visualeap.aliforreddit.domain.repository.PostRepository
import com.visualeap.aliforreddit.domain.util.scheduler.SchedulerProvider
import com.visualeap.aliforreddit.domain.util.applySchedulers
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@FragmentScope
class FrontPagePresenter @Inject constructor(
    private val view: FrontPageView,
    private val repository: PostRepository,
    private val schedulerProvider: SchedulerProvider
) {
    private val disposables = CompositeDisposable()

    fun start(feed: DefaultFeed) {
        val listingDisposable = Single.just(feed)
            .flatMap {
                when (it) {
                    DefaultFeed.Home -> repository.getHomePosts(false)
                    DefaultFeed.Popular -> repository.getPopularPosts(false)
                    else -> throw IllegalArgumentException("Wrong feed. Only home and popular are accepted")
                }
            }
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
                { /*onError*/ println(it.message) }
            )

        disposables.add(listingDisposable)
    }

    fun stop() {
        disposables.clear()
    }
}