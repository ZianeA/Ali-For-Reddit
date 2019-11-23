package com.visualeap.aliforreddit.data.repository.post

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.visualeap.aliforreddit.data.network.RedditService
import com.visualeap.aliforreddit.data.repository.feed.DefaultFeed
import com.visualeap.aliforreddit.data.repository.feed.FeedDao
import com.visualeap.aliforreddit.data.repository.post.postfeed.PostFeedDao
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
    private val feedDao: FeedDao,
    private val postFeedDao: PostFeedDao,
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

    //TODO refactor
    override fun getHomePosts(refresh: Boolean): Single<Listing<Post>> {
        return if (refresh) {
            //TODO fix this
//            nextPageKeyStore.put(PostBoundaryCallback.NEXT_PAGE_STORE_KEY, null)
            postDao.deleteAll()
                .andThen(getPostsByFeed(DefaultFeed.Home.name, DefaultFeed.Home.name))
        } else {
            getPostsByFeed(DefaultFeed.Home.name, DefaultFeed.Home.name)
        }
    }

    //TODO refactor
    override fun getPopularPosts(refresh: Boolean): Single<Listing<Post>> {
        return if (refresh) {
            //TODO fix this
//            nextPageKeyStore.put(PostBoundaryCallback.NEXT_PAGE_STORE_KEY, null)
            postDao.deleteAll()
                .andThen(getPostsByFeed(DefaultFeed.Popular.name, DefaultFeed.Popular.name))
        } else {
            getPostsByFeed(DefaultFeed.Popular.name, DefaultFeed.Popular.name)
        }
    }

    //TODO rename subredditFeed
    private fun getPostsByFeed(subredditFeed: String, feedId: String): Single<Listing<Post>> {
        return Single.fromCallable {
            val postFactory = postFeedDao.getPostsForFeed(feedId)
                .map(postWithSubredditEntityMapper::map)
            val postBoundaryCallback = PostBoundaryCallback(
                subredditFeed,
                redditService,
                postDao,
                feedDao,
                postFeedDao,
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
        //TODO change to 25
        private const val DATABASE_PAGE_SIZE = 30
    }
}