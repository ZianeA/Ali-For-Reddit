package com.visualeap.aliforreddit.data.repository.post

import androidx.paging.PagedList
import androidx.paging.toLiveData
import androidx.paging.toObservable
import com.airbnb.epoxy.EpoxyAsyncUtil
import com.visualeap.aliforreddit.data.network.RedditService
import com.visualeap.aliforreddit.data.repository.Mapper
import com.visualeap.aliforreddit.domain.model.Post
import com.visualeap.aliforreddit.domain.repository.PostRepository
import com.visualeap.aliforreddit.domain.util.NetworkState
import com.visualeap.aliforreddit.domain.util.scheduler.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostDataRepository @Inject constructor(
    private val postDao: PostDao,
    private val redditService: RedditService,
    private val schedulerProvider: SchedulerProvider,
    private val postWithRedditorMapper: Mapper<PostWithRedditor, Post>,
    private val postResponseMapper: @JvmSuppressWildcards Mapper<PostResponse, List<Post>>
) :
    PostRepository {
    override fun getPostsBySubreddit(subreddit: String): Observable<PagedList<Post>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getHomePosts(
        onNext: ((t: NetworkState) -> Unit),
        onError: ((t: Throwable) -> Unit)
    ): Observable<PagedList<Post>> {
        val postFactory = postDao.getAll().map(postWithRedditorMapper::map)
        val postBoundaryCallback = PostBoundaryCallback(
            redditService,
            postDao,
            schedulerProvider,
            postResponseMapper,
            postWithRedditorMapper
        )
        postBoundaryCallback.replay.subscribe()
//        val config = PagedList.Config.Builder()
//            .setPageSize(DATABASE_PAGE_SIZE)
//            .setEnablePlaceholders(true)
//            .build()
//        return RxPagedListBuilder(postFactory, config)
//            .setBoundaryCallback(postBoundaryCallback)
//            .buildObservable()

        return postDao.getAll()
            .map(postWithRedditorMapper::map)
            .toObservable(
                pageSize = DATABASE_PAGE_SIZE,
                boundaryCallback = postBoundaryCallback
            )
    }

    companion object {
        //TODO change to 20
        private const val DATABASE_PAGE_SIZE = 5
    }
}