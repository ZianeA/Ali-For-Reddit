package com.visualeap.aliforreddit.data.repository.post

import androidx.paging.PagedList
import com.visualeap.aliforreddit.data.network.RedditService
import com.visualeap.aliforreddit.data.repository.Mapper
import com.visualeap.aliforreddit.domain.model.Post
import com.visualeap.aliforreddit.domain.util.applySchedulers
import com.visualeap.aliforreddit.domain.util.scheduler.SchedulerProvider
import dagger.Reusable
import io.reactivex.Completable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Reusable
class PostBoundaryCallback @Inject constructor(
    private val redditService: RedditService,
    private val postDao: PostDao,
    private val schedulerProvider: SchedulerProvider,
    private val postResponseMapper: @JvmSuppressWildcards Mapper<PostResponse, List<Post>>,
    private val postWithRedditorMapper: Mapper<PostWithRedditor, Post>
) : PagedList.BoundaryCallback<Post>() {

    companion object {
        //TODO change to 50
        private const val NETWORK_PAGE_SIZE = 50
    }

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

        isRequestInProgress = true
        val disposable = redditService.getHomePosts(NETWORK_PAGE_SIZE, nextPageKey)
            /*.delay(10, TimeUnit.SECONDS)*/ //TODO remove this delay
            .map {
                nextPageKey = it.data.afterKey
                postResponseMapper.map(it)
            }
            .map { postList -> postList.map { postWithRedditorMapper.mapReverse(it) } }
            .flatMapCompletable { postWithRedditorList ->
                Completable.fromAction {
                    postWithRedditorList.forEach {
                        postDao.add(it.redditorEntity, it.subredditEntity, it.postEntity)
                    }
                }
            }
            .doFinally { isRequestInProgress = false }
            .applySchedulers(schedulerProvider)
            .subscribe({/*onSuccess*/}, {/*onError*/ println(it) })
    }
}