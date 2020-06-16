package com.visualeap.aliforreddit.presentation.main.frontPage

import com.visualeap.aliforreddit.domain.model.Post
import com.visualeap.aliforreddit.domain.model.Subreddit
import com.visualeap.aliforreddit.domain.model.feed.DefaultFeed.*
import com.visualeap.aliforreddit.domain.model.feed.SortType
import com.visualeap.aliforreddit.presentation.di.FragmentScope
import com.visualeap.aliforreddit.domain.usecase.FetchFeedPosts
import com.visualeap.aliforreddit.domain.util.applySchedulers
import com.visualeap.aliforreddit.domain.util.scheduler.SchedulerProvider
import com.visualeap.aliforreddit.presentation.util.SubredditFormatter
import com.visualeap.aliforreddit.presentation.util.formatCount
import com.visualeap.aliforreddit.presentation.util.formatTimestamp
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.BehaviorProcessor
import javax.inject.Inject
import kotlin.math.max

@FragmentScope
class FrontPagePresenter @Inject constructor(
    private val view: FrontPageView,
    private val fetchFeedPosts: FetchFeedPosts,
    private val schedulerProvider: SchedulerProvider
) {
    companion object {
        private const val PAGE_SIZE = 25
        private const val PAGINATION_STEP: Int = PAGE_SIZE / 4
    }

    private val disposables = CompositeDisposable()
    private var lastOffset = 0
    private val offsetProcessor = BehaviorProcessor.createDefault(lastOffset)

    fun start(feed: String) {
        val disposable = offsetProcessor.distinctUntilChanged()
            .switchMap { offset ->
                when (feed) {
                    Home.name -> TODO("Not implemented")
                    else -> fetchFeedPosts.execute(feed, SortType.Hot, offset, PAGE_SIZE)
                }
                    .map<FrontPageViewState> { listing ->
                        lastOffset = listing.offset
                        FrontPageViewState.Success(
                            listing.reachedTheEnd,
                            listing.items.map { (s, p) -> formatPost(s, p) })
                    }
                    .onErrorReturn { FrontPageViewState.Failure }
                    .applySchedulers(schedulerProvider)
                    .startWith(FrontPageViewState.Loading)
            }
            .subscribe(view::render)

        disposables.add(disposable)
    }

    fun onScroll(firstVisiblePostPosition: Int, lastVisiblePostPosition: Int) {
        if (addItemsAtTop(firstVisiblePostPosition)) {
            // Keep the offset above zero.
            val nextOffset = max(0, lastOffset - PAGINATION_STEP)
            offsetProcessor.offer(nextOffset)
        } else if (addItemsAtBottom(lastVisiblePostPosition)) {
            offsetProcessor.offer(lastOffset + PAGINATION_STEP)
        }
    }

    fun stop() {
        disposables.clear()
    }

    private fun addItemsAtTop(position: Int) = position < PAGE_SIZE / 4

    private fun addItemsAtBottom(position: Int) = position > PAGE_SIZE * 3 / 4

    private fun formatPost(subreddit: Subreddit, post: Post): FeedPostDto {
        return FeedPostDto(
            post.id,
            "u/${post.authorName}",
            post.title,
            post.text,
            formatCount(post.score),
            formatCount(post.commentCount),
            formatTimestamp(post.created),
            subreddit.id,
            "r/${subreddit.name}",
            SubredditFormatter.formatColor(subreddit.primaryColor, subreddit.keyColor),
            SubredditFormatter.formatIcon(subreddit.iconUrl)
        )
    }
}