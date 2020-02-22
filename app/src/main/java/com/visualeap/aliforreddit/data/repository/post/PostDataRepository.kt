package com.visualeap.aliforreddit.data.repository.post

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.visualeap.aliforreddit.data.network.RedditService
import com.visualeap.aliforreddit.domain.model.Feed.DefaultFeed
import com.visualeap.aliforreddit.data.repository.feed.FeedDao
import com.visualeap.aliforreddit.data.repository.feed.FeedEntity
import com.visualeap.aliforreddit.data.repository.post.postfeed.PostFeedDao
import com.visualeap.aliforreddit.domain.model.Feed.FeedType
import com.visualeap.aliforreddit.domain.model.Feed.SortBy
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
            postDao.deleteAll()
                .andThen(getPostsByFeed(DefaultFeed.Home.name, FeedType.Default))
        } else {
            getPostsByFeed(DefaultFeed.Home.name, FeedType.Default)
        }
    }

    //TODO refactor
    override fun getPopularPosts(refresh: Boolean): Single<Listing<Post>> {
        return if (refresh) {
            //TODO fix this
            postDao.deleteAll()
                .andThen(getPostsByFeed(DefaultFeed.Popular.name, FeedType.Default))
        } else {
            getPostsByFeed(DefaultFeed.Popular.name, FeedType.Default)
        }
    }

    private fun getPostsByFeed(feedName: String, feedType: FeedType): Single<Listing<Post>> {
        return feedDao.getByName(feedName)
            .switchIfEmpty(
                feedDao.add(FeedEntity(feedName, SortBy.Best, null, feedType))
                    .andThen(feedDao.getByName(feedName))
                    .toSingle()
            )
            .flatMap {
                Single.fromCallable {
                    val postFactory = postFeedDao.getPostsForFeed(feedName)
                        .map(postWithSubredditEntityMapper::map)
                    val postBoundaryCallback = PostBoundaryCallback(
                        it,
                        redditService,
                        postDao,
                        feedDao,
                        postFeedDao,
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
    }

    companion object {
        //TODO change to 25
        private const val DATABASE_PAGE_SIZE = 30
    }
}