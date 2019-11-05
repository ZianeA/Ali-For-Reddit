package com.visualeap.aliforreddit.data.repository.post

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.visualeap.aliforreddit.data.network.RedditService
import com.visualeap.aliforreddit.domain.util.Mapper
import com.visualeap.aliforreddit.domain.model.Post
import com.visualeap.aliforreddit.domain.repository.Listing
import com.visualeap.aliforreddit.domain.repository.PostRepository
import com.visualeap.aliforreddit.domain.util.scheduler.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostDataRepository @Inject constructor(
    private val postDao: PostDao,
    private val redditService: RedditService,
    private val nextPageKeyStore: KeyValueStore<String?>,
    private val schedulerProvider: SchedulerProvider,
    private val postWithSubredditEntityMapper: Mapper<PostWithSubredditEntity, Post>,
    private val postWithSubredditResponseMapper: @JvmSuppressWildcards Mapper<PostWithSubredditResponse, List<Post>>
) :
    PostRepository {
    override fun getPostById(id: String): Single<Post> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPostsBySubreddit(subreddit: String): Observable<PagedList<Post>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getHomePosts(refresh: Boolean): Single<Listing<Post>> {
        return if (refresh) {
            nextPageKeyStore.put(PostBoundaryCallback.NEXT_PAGE_STORE_KEY, null)
            postDao.deleteAll()
                .andThen(getHomePosts())
        } else {
            getHomePosts()
        }
    }

    private fun getHomePosts(): Single<Listing<Post>> {
        return Single.fromCallable {
            val postFactory = postDao.getAll().map(postWithSubredditEntityMapper::map)
            val postBoundaryCallback = PostBoundaryCallback(
                redditService,
                postDao,
                nextPageKeyStore,
                schedulerProvider,
                postWithSubredditResponseMapper,
                postWithSubredditEntityMapper
            )
            val config = PagedList.Config.Builder()
                .setPageSize(DATABASE_PAGE_SIZE)
                .setEnablePlaceholders(false)
                .build()

            val pagedList = RxPagedListBuilder(postFactory, config)
                .setBoundaryCallback(postBoundaryCallback)
                .buildObservable()
                .doOnDispose(postBoundaryCallback::dispose)

            Listing(
                pagedList = pagedList,
                networkState = postBoundaryCallback.networkStateReplay,
                retry = { postBoundaryCallback.retry() })
        }
    }

    companion object {
        //TODO change to 20
        private const val DATABASE_PAGE_SIZE = 5
    }
}