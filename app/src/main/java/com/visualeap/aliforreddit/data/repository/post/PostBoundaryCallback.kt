package com.visualeap.aliforreddit.data.repository.post

import androidx.paging.PagedList
import com.jakewharton.rxrelay2.BehaviorRelay
import com.visualeap.aliforreddit.data.network.RedditService
import com.visualeap.aliforreddit.data.repository.Mapper
import com.visualeap.aliforreddit.domain.model.Post
import com.visualeap.aliforreddit.domain.util.NetworkState
import com.visualeap.aliforreddit.domain.util.applySchedulers
import com.visualeap.aliforreddit.domain.util.scheduler.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.Single

//TODO add unit tests
class PostBoundaryCallback(
    private val redditService: RedditService,
    private val postDao: PostDao,
    private val schedulerProvider: SchedulerProvider,
    private val postWithSubredditResponseMapper: @JvmSuppressWildcards Mapper<PostWithSubredditResponse, List<Post>>,
    private val postWithSubredditEntityMapper: Mapper<PostWithSubredditEntity, Post>
) : PagedList.BoundaryCallback<Post>() {

    companion object {
        //TODO change to 50
        private const val NETWORK_PAGE_SIZE = 50
    }

    val replay: BehaviorRelay<NetworkState> = BehaviorRelay.create()
    private var isRequestInProgress = false
    private var nextPageKey: String? = null

    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        requestAndSaveData()
    }

    override fun onItemAtEndLoaded(itemAtEnd: Post) {
        super.onItemAtEndLoaded(itemAtEnd)
        requestAndSaveData()
    }

    private fun requestAndSaveData() {
        if (isRequestInProgress) return

        replay.accept(NetworkState.LOADING)
        isRequestInProgress = true
        val disposable = redditService.getHomePosts(NETWORK_PAGE_SIZE, nextPageKey)
            .flatMap { postResponse ->
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
            .map {
                nextPageKey = it.postResponse.data.afterKey
                postWithSubredditResponseMapper.map(it)
            }
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
                { replay.accept(NetworkState.LOADED) },
                { replay.accept(NetworkState.error(it)) })
    }
}