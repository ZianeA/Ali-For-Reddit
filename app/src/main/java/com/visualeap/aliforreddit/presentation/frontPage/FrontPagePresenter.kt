package com.visualeap.aliforreddit.presentation.frontPage

import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.domain.feed.SortType
import com.visualeap.aliforreddit.presentation.common.di.FragmentScope
import com.visualeap.aliforreddit.domain.post.FetchFeedPosts
import com.visualeap.aliforreddit.domain.util.Lce
import com.visualeap.aliforreddit.domain.util.autoReplay
import com.visualeap.aliforreddit.domain.util.scheduler.SchedulerProvider
import com.visualeap.aliforreddit.presentation.common.formatter.PostFormatter
import com.visualeap.aliforreddit.presentation.common.util.ResourceProvider
import com.visualeap.aliforreddit.presentation.frontPage.FrontPageResult.*
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.roundToInt

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
        private const val PAGINATION_STEP: Int = PAGE_SIZE / 5
    }

    private lateinit var disposable: Disposable
    private var lastOffset = 0
    private val offsetSubject = BehaviorSubject.createDefault(lastOffset)

    private val events: PublishSubject<FrontPageEvent> = PublishSubject.create()

    val viewState: Observable<FrontPageViewState> by lazy {
        events
            .doOnNext { Timber.d("----- event $it") }
            .eventToResult()
            .doOnNext { Timber.d("----- result $it") }
            .resultToViewState()
            .doOnNext { Timber.d("----- viewstate $it") }
            .autoReplay { disposable = it }
    }

    private fun Observable<FrontPageEvent>.eventToResult(): Observable<FrontPageResult> {
        return publish { event ->
            Observable.merge(
                event.ofType(FrontPageEvent.ScreenLoadEvent::class.java).take(1).handleScreenLoad(),
                event.ofType(FrontPageEvent.PostBoundEvent::class.java).handlePostBound()
            )
        }
    }

    fun passEvent(event: FrontPageEvent) = events.onNext(event)

    private fun Observable<FrontPageEvent.ScreenLoadEvent>.handleScreenLoad(): Observable<ScreenLoadResult> {
        return flatMap { _ ->
            offsetSubject.distinctUntilChanged()
                .switchMap { offset -> loadPosts(offset).subscribeOn(schedulerProvider.io) }
                .observeOn(schedulerProvider.ui)
                .startWith(ScreenLoadResult.Loading)
        }
    }

    private fun loadPosts(offset: Int): Observable<ScreenLoadResult> {
        return fetchFeedPosts.execute(feed, SortType.Hot, offset, PAGE_SIZE)
            .toObservable()
            .map { lce ->
                when (lce) {
                    is Lce.Loading -> ScreenLoadResult.Loading
                    is Lce.Content -> {
                        val listing = lce.data
                        lastOffset = listing.offset
                        ScreenLoadResult.Content(
                            listing.items.map { (s, p) -> PostFormatter.formatPost(s, p) },
                            !listing.reachedTheEnd
                        )
                    }
                    is Lce.Error -> {
                        ScreenLoadResult.Error(resourceProvider.getString(R.string.error_server))
                    }
                }
            }
    }

    private fun Observable<FrontPageEvent.PostBoundEvent>.handlePostBound(): Observable<PostBoundResult> {
        return distinctUntilChanged().doOnNext { event ->
            if (addItemsAtTheTop(event.isScrollingDown, event.position)) {
                // Keep the offset above zero.
                val nextOffset = max(0, lastOffset - PAGINATION_STEP)
                offsetSubject.onNext(nextOffset)
            } else if (addItemsAtTheBottom(event.isScrollingDown, event.position)) {
                val nextOffset = lastOffset + PAGINATION_STEP
                offsetSubject.onNext(nextOffset)
            }
        }
            .ignoreElements()
            .toObservable()
    }

    private fun Observable<FrontPageResult>.resultToViewState(): Observable<FrontPageViewState> {
        return scan(FrontPageViewState()) { vs, result ->
            when (result) {
                is ScreenLoadResult -> when (result) {
                    ScreenLoadResult.Loading -> vs.copy(loading = true)
                    is ScreenLoadResult.Content -> {
                        vs.copy(
                            posts = result.posts,
                            loading = false,
                            loadingMore = result.loadingMore
                        )
                    }
                    is ScreenLoadResult.Error -> vs.copy(error = result.error, loading = false)
                }
                is PostBoundResult -> vs
            }
        }
            .distinctUntilChanged()
    }

    fun onCleared() {
        disposable.dispose()
    }

    private fun addItemsAtTheTop(isScrollingDown: Boolean, position: Int) =
        !isScrollingDown && position <= (PAGE_SIZE / 2f).roundToInt() - 2

    private fun addItemsAtTheBottom(isScrollingDown: Boolean, position: Int) =
        isScrollingDown && position >= (PAGE_SIZE / 2f).roundToInt()
}