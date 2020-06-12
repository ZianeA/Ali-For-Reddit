package com.visualeap.aliforreddit.presentation.main.frontPage

import android.graphics.Color
import com.visualeap.aliforreddit.domain.model.feed.DefaultFeed
import com.visualeap.aliforreddit.domain.model.feed.SortType
import com.visualeap.aliforreddit.presentation.di.FragmentScope
import com.visualeap.aliforreddit.domain.repository.PostRepository
import com.visualeap.aliforreddit.domain.usecase.FetchFeedPosts
import com.visualeap.aliforreddit.domain.util.applySchedulers
import com.visualeap.aliforreddit.domain.util.scheduler.SchedulerProvider
import com.visualeap.aliforreddit.presentation.util.formatCount
import com.visualeap.aliforreddit.presentation.util.formatTimestamp
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.BehaviorProcessor
import javax.inject.Inject

@FragmentScope
class FrontPagePresenter @Inject constructor(
    private val view: FrontPageView,
    private val fetchFeedPosts: FetchFeedPosts,
    private val schedulerProvider: SchedulerProvider
) {
    companion object {
        private const val DEFAULT_COLOR = "#33a8ff"
        private const val PAGE_SIZE = 25
        private const val TOP_PAGINATION_POINT = PAGE_SIZE / 4
        private const val BOTTOM_PAGINATION_POINT = PAGE_SIZE * 3 / 4
        private const val PAGINATION_STEP: Int = PAGE_SIZE / 4
    }

    private val disposables = CompositeDisposable()
    private var lastOffset = 0
    private val offsetProcessor = BehaviorProcessor.createDefault(lastOffset)

    fun start(feed: DefaultFeed) {
        val disposable = offsetProcessor
            .distinctUntilChanged()
            .switchMap { offset ->
                when (feed) {
                    DefaultFeed.Home -> fetchFeedPosts.execute(
                        DefaultFeed.Popular.name,
                        SortType.Hot,
                        0,
                        20
                    )
                    DefaultFeed.Popular -> fetchFeedPosts.execute(
                        DefaultFeed.Popular.name,
                        SortType.Hot,
                        offset,
                        PAGE_SIZE
                    )
                    else -> throw IllegalArgumentException()
                }
                    .map<FrontPageViewState> { listing ->
                        lastOffset = listing.offset
                        FrontPageViewState.Success(
                            listing.reachedTheEnd,
                            listing.items.map { (s, p) ->
                                FeedPostDto(
                                    p.id,
                                    "u/${p.authorName}",
                                    p.title,
                                    p.text,
                                    formatCount(p.score),
                                    formatCount(p.commentCount),
                                    formatTimestamp(p.created),
                                    s.id,
                                    "r/${s.name}",
                                    s.iconUrl,
                                    s.primaryColor ?: s.keyColor ?: DEFAULT_COLOR
                                )
                            })
                    }
                    .onErrorReturn { FrontPageViewState.Failure }
                    .applySchedulers(schedulerProvider)
                    .startWith(FrontPageViewState.Loading)
            }
            .subscribe { viewState ->
                when (viewState) {
                    FrontPageViewState.Loading -> ""
                    FrontPageViewState.Failure -> ""
                    is FrontPageViewState.Success -> view.displayPosts(viewState.posts)
                }
            }

        disposables.add(disposable)
    }

    fun onPostBound(position: Int) {
        if (addItemsAtTop(position)) {
            val nextOffset = lastOffset - PAGINATION_STEP
            offsetProcessor.offer(nextOffset)
        } else if (addItemsAtBottom(position)) {
            val nextOffset = lastOffset + PAGINATION_STEP
            offsetProcessor.offer(nextOffset)
        }
    }

    fun stop() {
        disposables.clear()
    }

    private fun addItemsAtTop(position: Int): Boolean = position == TOP_PAGINATION_POINT - 1

    private fun addItemsAtBottom(position: Int): Boolean = position == BOTTOM_PAGINATION_POINT + 1
}