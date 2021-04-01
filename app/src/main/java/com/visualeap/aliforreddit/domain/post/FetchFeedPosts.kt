package com.visualeap.aliforreddit.domain.post

import com.visualeap.aliforreddit.data.post.Post
import com.visualeap.aliforreddit.data.post.PostResponse
import com.visualeap.aliforreddit.data.post.PostResponseMapper
import com.visualeap.aliforreddit.data.post.PostWebService
import com.visualeap.aliforreddit.data.subreddit.*
import com.visualeap.aliforreddit.domain.feed.DefaultFeed
import com.visualeap.aliforreddit.domain.feed.FeedRepository
import com.visualeap.aliforreddit.domain.feed.SortType
import com.visualeap.aliforreddit.domain.util.Lce
import com.visualeap.aliforreddit.domain.util.toLce
import dagger.Reusable
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.rxkotlin.Singles
import javax.inject.Inject
import kotlin.math.min

@Reusable
class FetchFeedPosts @Inject constructor(
    private val postRepository: PostRepository,
    private val subredditDao: SubredditDao,
    private val postService: PostWebService,
    private val subredditService: SubredditWebService,
    private val afterKeyRepository: AfterKeyRepository,
    private val feedRepository: FeedRepository
) {
    fun execute(
        feed: String,
        sortType: SortType,
        offset: Int,
        pageSize: Int
    ): Flowable<Lce<Listing<Pair<Subreddit, Post>>>> {
        return Singles.zip(
            postRepository.countPostsByFeed(feed, sortType),
            afterKeyRepository.getAfterKey(feed, sortType)
        ).map { (count, afterKey) ->
            val correctedPageSize = min(count, pageSize)
            val correctedOffset = when {
                offset < 0 -> 0
                offset + correctedPageSize > count -> count - correctedPageSize
                else -> offset
            }
            Triple(correctedPageSize, correctedOffset, afterKey)

        }.flatMapPublisher { (correctedPageSize, correctedOffset, afterKey) ->
            val endOfRemote = afterKey is AfterKey.End
            // If we have reached the end of cached items
            // but not the end of the remote ones, fetch new items
            val endOfCache = isEndOfCache(correctedPageSize, pageSize, correctedOffset, offset)
            if (endOfCache && !endOfRemote) {
                // Return the cached items first (first observable)
                // while fetching remote items (second observable)
                // The cache is only returned if it's not empty.
                loadFromNetwork(feed, sortType, pageSize, offset, afterKey).publish { network ->
                    Flowable.merge(
                        loadFromCache(
                            feed, sortType, correctedPageSize, correctedOffset, endOfRemote
                        )
                            .takeUntil(network)
                            .filterEmpty()
                            .distinctUntilChanged(),
                        network
                    )
                }
            } else {
                // If not, just return the cached items.
                loadFromCache(feed, sortType, correctedPageSize, correctedOffset, endOfRemote)
            }
        }
    }

    private fun isEndOfCache(
        correctedPageSize: Int,
        pageSize: Int,
        correctedOffset: Int,
        offset: Int
    ) = correctedPageSize == 0 || correctedPageSize < pageSize || correctedOffset < offset

    private fun loadFromCache(
        feed: String,
        sortType: SortType,
        correctedPageSize: Int,
        correctedOffset: Int,
        reachedTheEnd: Boolean
    ): Flowable<Lce<Listing<Pair<Subreddit, Post>>>> {
        return postRepository.getPostsByFeed(feed, sortType, correctedOffset, correctedPageSize)
            .flatMap { posts ->
                val subredditIds = posts.map { it.subredditId }
                // TODO get subreddits by post id for better performance
                subredditDao.getByIds(subredditIds)
                    .distinct()
                    .map { subredditList ->
                        // Transform the list of subreddits into a map (id -> Subreddit) for faster
                        // lookups when joining each post with its subreddit.
                        val subredditMap = subredditList.associateBy { it.id }
                        val subredditPostList =
                            posts.map { post -> subredditMap.getValue(post.subredditId) to post }
                        Listing(subredditPostList, correctedOffset, reachedTheEnd)
                    }.toLce()
            }
    }

    private fun loadFromNetwork(
        feed: String,
        sortType: SortType,
        pageSize: Int,
        offset: Int,
        afterKey: AfterKey
    ): Flowable<Lce<Listing<Pair<Subreddit, Post>>>> {
        return feedRepository.addFeed(feed)
            .andThen(getPostsByFeed(feed, pageSize, afterKey))
            .map { response -> response.getAfterKey() to PostResponseMapper.map(response) }
            .flatMapCompletable { (afterKey, remotePost) ->
                afterKeyRepository.setAfterKey(feed, sortType, afterKey)
                    .andThen(savePost(remotePost, feed, sortType))
            }
            .andThen(execute(feed, sortType, offset, pageSize))
            // Although the above operation returns an LCE, it will not, however, prevent the stream
            // from terminating if fetching posts fails. The andThen operator doesn't run after an error.
            .onErrorReturn { Lce.Error(it) }
    }

    private fun getPostsByFeed(
        feed: String,
        pageSize: Int,
        afterKey: AfterKey
    ): Single<PostResponse> {
        return if (feed == DefaultFeed.Home.name) {
            postService.getHomePosts(pageSize, afterKey.toStringOrNull())
        } else {
            postService.getPostsBySubreddit(feed, pageSize, afterKey.toStringOrNull())
        }
    }

    private fun savePost(
        remotePost: List<Post>,
        feed: String,
        sortType: SortType
    ): Completable {
        return if (remotePost.isNotEmpty()) {
            // For each post, fetch the subreddit it belongs to
            val subredditIds = remotePost.joinToString { it.subredditId }
            subredditService.getSubredditsByIds(subredditIds)
                .map(SubredditResponseMapper::map)
                .flatMapCompletable { remoteSubreddits ->
                    // The subreddit must be added first because of the foreign key constraint
                    subredditDao.addAll(remoteSubreddits)
                        .andThen(postRepository.addPosts(remotePost, feed, sortType))
                }
        } else Completable.complete()
    }

    private fun PostResponse.getAfterKey(): AfterKey {
        return when (val key = this.data.afterKey) {
            null -> AfterKey.End
            else -> AfterKey.Next(key)
        }
    }

    private fun Flowable<Lce<Listing<Pair<Subreddit, Post>>>>.filterEmpty(): Flowable<Lce<Listing<Pair<Subreddit, Post>>>> {
        return filter { lce ->
            if (lce is Lce.Content) {
                lce.data.items.isNotEmpty()
            } else {
                true
            }
        }
    }
}