package com.visualeap.aliforreddit.data.repository.post

import androidx.paging.PagedList
import com.jakewharton.rxrelay2.BehaviorRelay
import com.visualeap.aliforreddit.data.network.RedditService
import com.visualeap.aliforreddit.domain.util.Mapper
import com.visualeap.aliforreddit.domain.model.Post
import com.visualeap.aliforreddit.domain.repository.NetworkState
import com.visualeap.aliforreddit.domain.util.applySchedulers
import com.visualeap.aliforreddit.domain.util.scheduler.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable

//TODO add unit tests
class PostBoundaryCallback(
    private val redditService: RedditService,
    private val postDao: PostDao,
    private val nextPageKeyStore: KeyValueStore<String?>,
    private val schedulerProvider: SchedulerProvider,
    private val postWithSubredditResponseMapper: @JvmSuppressWildcards Mapper<PostWithSubredditResponse, List<Post>>,
    private val postWithSubredditEntityMapper: Mapper<PostWithSubredditEntity, Post>
) : PagedList.BoundaryCallback<Post>() {

    companion object {
        //TODO change to 25
        private const val NETWORK_PAGE_SIZE = 5
        const val NEXT_PAGE_STORE_KEY = "next_page_key"
    }

    val networkStateReplay: BehaviorRelay<NetworkState> = BehaviorRelay.create()
    private var nextPageKey: String? = nextPageKeyStore.get(NEXT_PAGE_STORE_KEY, null)
        set(value) {
            field = value
            nextPageKeyStore.put(NEXT_PAGE_STORE_KEY, value)
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
        val disposable = redditService.getHomePosts(NETWORK_PAGE_SIZE, nextPageKey)
            .flatMap { postResponse ->
                nextPageKey = postResponse.data.afterKey
                val subredditIds = postResponse.data.postHolders.map { it.post.subredditId }

                redditService.getSubreddit(subredditIds.joinToString())
                    .map { subredditResponse ->
                        PostWithSubredditResponse(
                            postResponse,
                            subredditResponse
                        )
                    }
            }
            /*.delay(10, TimeUnit.SECONDS)*/ //TODO remove this delay
            .map(postWithSubredditResponseMapper::map)
            .map { postList -> postList.map { postWithSubredditEntityMapper.mapReverse(it) } }
            .flatMapCompletable { postWithSubredditEntityList ->
                Completable.fromAction {
                    postWithSubredditEntityList.forEach {
                        postDao.add(it.subredditEntity, it.postEntity)
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