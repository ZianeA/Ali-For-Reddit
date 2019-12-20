package com.visualeap.aliforreddit.data.repository.post

import androidx.paging.PagedList
import com.jakewharton.rxrelay2.BehaviorRelay
import com.visualeap.aliforreddit.data.network.RedditService
import com.visualeap.aliforreddit.data.repository.feed.DefaultFeed
import com.visualeap.aliforreddit.data.repository.feed.FeedDao
import com.visualeap.aliforreddit.data.repository.post.postfeed.PostFeedDao
import com.visualeap.aliforreddit.data.repository.post.postfeed.PostFeedEntity
import com.visualeap.aliforreddit.domain.util.Mapper
import com.visualeap.aliforreddit.domain.model.Post
import com.visualeap.aliforreddit.domain.repository.NetworkState
import com.visualeap.aliforreddit.domain.util.applySchedulers
import com.visualeap.aliforreddit.domain.util.scheduler.SchedulerProvider
import io.reactivex.*
import io.reactivex.disposables.CompositeDisposable

//TODO add unit tests
class PostBoundaryCallback(
    private val feedOrSubreddit: String, //TODO rename
    private val redditService: RedditService,
    private val postDao: PostDao,
    private val feedDao: FeedDao,
    private val postFeedDao: PostFeedDao,
    private val nextPageKeyStore: KeyValueStore<String?>,
    private val schedulerProvider: SchedulerProvider,
    private val postWithSubredditResponseMapper: @JvmSuppressWildcards Mapper<PostWithSubredditResponse, List<Post>>,
    private val postWithSubredditEntityMapper: Mapper<PostWithSubredditEntity, Post>
) : PagedList.BoundaryCallback<Post>() {

    companion object {
        //TODO change to 25
        private const val NETWORK_PAGE_SIZE = 25
//        const val NEXT_PAGE_STORE_KEY = "next_page_key"
    }

    val networkStateReplay: BehaviorRelay<NetworkState> = BehaviorRelay.create()
    private var nextPageKey: String? = nextPageKeyStore.get(feedOrSubreddit, null)
        set(value) {
            field = value
            nextPageKeyStore.put(feedOrSubreddit, value)
        }
    private var isRequestInProgress = false
    private val disposables = CompositeDisposable()

    // keep a function reference for the retry event
    private var retry: (() -> Any)? = null

    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        requestAndSaveData()
        retry = { onZeroItemsLoaded() }
    }

    override fun onItemAtEndLoaded(itemAtEnd: Post) {
        super.onItemAtEndLoaded(itemAtEnd)
        requestAndSaveData()
        retry = { onItemAtEndLoaded(itemAtEnd) }
    }

    fun dispose() {
        disposables.clear()
    }

    fun retry() {
        retry?.invoke()
    }

    private fun requestAndSaveData() {
        if (isRequestInProgress) return

        networkStateReplay.accept(NetworkState.LOADING)
        isRequestInProgress = true

        val disposable = Single.just(feedOrSubreddit.equals(DefaultFeed.Home.name, true))
            .flatMap {
                if (it) {
                    redditService.getHomePosts(NETWORK_PAGE_SIZE, nextPageKey)
                } else {
                    redditService.getPostsBySubreddit(
                        feedOrSubreddit,
                        NETWORK_PAGE_SIZE,
                        nextPageKey
                    )
                }
            }
            .flatMap { postResponse ->
                nextPageKey = postResponse.data.afterKey
                val subredditIds = postResponse.data.postHolders.map { it.post.subredditId }

                redditService.getSubreddits(subredditIds.joinToString())
                    .map { subredditResponse ->
                        PostWithSubredditResponse(
                            postResponse,
                            subredditResponse
                        )
                    }
            }
            .map(postWithSubredditResponseMapper::map)
            .map { postList -> postList.map { postWithSubredditEntityMapper.mapReverse(it) } }
            .flatMapCompletable { postWithSubredditEntityList ->
                feedDao.getByName(feedOrSubreddit)
                    .isEmpty
                    .map { !it }
                    .flatMapCompletable { feedExists ->
                        Observable.fromIterable(postWithSubredditEntityList.withIndex())
                            .flatMapCompletable {
                                Completable.fromAction {
                                    postDao.add(it.value.subredditEntity, it.value.postEntity)
                                }
                                    .andThen(Completable.defer {
                                        if (feedExists.not()) {
                                            Completable.complete()
                                        } else {
                                            postFeedDao.add(
                                                PostFeedEntity(
                                                    it.value.postEntity.id,
                                                    feedOrSubreddit,
                                                    it.index //TODO post position depends on pagesize and ...
                                                )
                                            )
                                        }
                                    })
                            }

                    }
            }
            .doFinally { isRequestInProgress = false }
            .applySchedulers(schedulerProvider)
            .subscribe(
                { networkStateReplay.accept(NetworkState.LOADED) },
                { networkStateReplay.accept(NetworkState.error(it)) })

        disposables.add(disposable)
    }
}