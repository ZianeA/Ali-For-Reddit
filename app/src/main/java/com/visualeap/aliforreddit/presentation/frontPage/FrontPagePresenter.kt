package com.visualeap.aliforreddit.presentation.frontPage

import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.domain.post.Post
import com.visualeap.aliforreddit.domain.subreddit.Subreddit
import com.visualeap.aliforreddit.domain.feed.SortType
import com.visualeap.aliforreddit.presentation.common.di.FragmentScope
import com.visualeap.aliforreddit.domain.post.FetchFeedPosts
import com.visualeap.aliforreddit.domain.util.applySchedulers
import com.visualeap.aliforreddit.domain.util.autoReplay
import com.visualeap.aliforreddit.domain.util.scheduler.SchedulerProvider
import com.visualeap.aliforreddit.presentation.common.formatter.PostFormatter
import com.visualeap.aliforreddit.presentation.common.util.ResourceProvider
import com.visualeap.aliforreddit.presentation.common.formatter.SubredditFormatter
import com.visualeap.aliforreddit.presentation.common.formatter.formatCount
import com.visualeap.aliforreddit.presentation.common.formatter.formatTimestamp
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.BehaviorProcessor
import javax.inject.Inject
import kotlin.math.min

@FragmentScope
class FrontPagePresenter @Inject constructor(
    private val launcher: FrontPageLauncher,
    @Feed private val feed: String,
    private val fetchFeedPosts: FetchFeedPosts,
    private val resourceProvider: ResourceProvider,
    private val schedulerProvider: SchedulerProvider
) {
    companion object {
        private const val PAGE_SIZE = 25
        private const val PAGINATION_STEP: Int = PAGE_SIZE / 4
    }

    private lateinit var disposable: Disposable
    private var lastOffset = 0
    private val offsetProcessor = BehaviorProcessor.createDefault(lastOffset)
    private val posts = offsetProcessor.distinctUntilChanged()
        .switchMap { offset ->
            fetchFeedPosts.execute(feed, SortType.Hot, offset, PAGE_SIZE)
                .map<FrontPageViewState> { listing ->
                    lastOffset = listing.offset
                    FrontPageViewState.Success(
                        !listing.reachedTheEnd,
                        listing.items.map { (s, p) -> PostFormatter.formatPost(s, p) })
                }
                .onErrorReturn { FrontPageViewState.Failure(resourceProvider.getString(R.string.error_server)) }
                .applySchedulers(schedulerProvider)
                .startWith(FrontPageViewState.Loading)
        }
        .autoReplay { disposable = it }

    fun start(): Flowable<FrontPageViewState> {
        return posts
    }

    fun onPostBound(position: Int) {
        if (addItemsAtTop(position)) {
            // Keep the offset above zero.
            val nextOffset = min(0, lastOffset - PAGINATION_STEP)
            offsetProcessor.offer(nextOffset)
        } else if (addItemsAtBottom(position)) {
            offsetProcessor.offer(lastOffset + PAGINATION_STEP)
        }
    }

    fun onCleared() {
        disposable.dispose()
    }

    private fun addItemsAtTop(position: Int) = position == PAGE_SIZE / 4 - 1

    private fun addItemsAtBottom(position: Int) = position == PAGE_SIZE * 3 / 4 + 1
}