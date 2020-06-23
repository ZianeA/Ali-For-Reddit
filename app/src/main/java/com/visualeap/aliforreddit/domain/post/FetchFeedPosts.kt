package com.visualeap.aliforreddit.domain.post

import com.visualeap.aliforreddit.data.post.PostResponse
import com.visualeap.aliforreddit.data.post.PostResponseMapper
import com.visualeap.aliforreddit.data.post.PostWebService
import com.visualeap.aliforreddit.data.subreddit.SubredditResponse
import com.visualeap.aliforreddit.data.subreddit.SubredditResponseMapper
import com.visualeap.aliforreddit.data.subreddit.SubredditWebService
import com.visualeap.aliforreddit.domain.subreddit.Subreddit
import com.visualeap.aliforreddit.domain.feed.DefaultFeed
import com.visualeap.aliforreddit.domain.feed.FeedRepository
import com.visualeap.aliforreddit.domain.feed.SortType
import com.visualeap.aliforreddit.domain.subreddit.SubredditRepository
import dagger.Reusable
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject
import kotlin.math.min

@Reusable
class FetchFeedPosts @Inject constructor(
    private val postRepository: PostRepository,
    private val subredditRepository: SubredditRepository,
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
    ): Flowable<Listing<Pair<Subreddit, Post>>> {
        return postRepository.countPostsByFeed(feed, sortType)
            .map { count ->
                val correctedPageSize = min(count, pageSize)
                val correctedOffset = when {
                    offset < 0 -> 0
                    offset + correctedPageSize > count -> count - correctedPageSize
                    else -> offset
                }
                correctedPageSize to correctedOffset
            }
            .flatMapPublisher { (correctedPageSize, correctedOffset) ->
                postRepository.getPostsByFeed(feed, sortType, correctedOffset, correctedPageSize)
                    // Ignore emitted items until concatMap completes. All changes in the database will be ignored.
                    .onBackpressureDrop()
                    .concatMap { cachedPosts ->
                        afterKeyRepository.getAfterKey(feed, sortType)
                            .flatMapPublisher { afterKey ->
                                val endOfRemote = afterKey is AfterKey.End
                                // If we have reached the end of cached items
                                // but not the end of the remote ones, fetch new items
                                val endOfCache = isEndOfCache(
                                    correctedPageSize, pageSize, correctedOffset, offset
                                )
                                if (endOfCache && !endOfRemote) {
                                    // Return the cached items first (first observable)
                                    // while fetching remote items (second observable)
                                    // The cache is only returned if it's not empty.
                                    Flowable.merge(
                                        loadFromCache(cachedPosts, endOfRemote, correctedOffset)
                                            .filter { it.items.isNotEmpty() },
                                        loadFromNetwork(feed, pageSize, sortType, afterKey, offset)
                                    )
                                } else {
                                    // If not, just return the cached items.
                                    loadFromCache(cachedPosts, endOfRemote, correctedOffset)
                                }
                            }
                    }
            }
    }

    private fun isEndOfCache(
        correctedPageSize: Int,
        pageSize: Int,
        correctedOffset: Int,
        offset: Int
    ) =
        correctedPageSize == 0 || correctedPageSize < pageSize || correctedOffset < offset

    private fun loadFromCache(
        cachedPosts: List<Post>,
        reachedTheEnd: Boolean,
        correctedOffset: Int
    ): Flowable<Listing<Pair<Subreddit, Post>>> {
        val subredditIds = cachedPosts.map { it.subredditId }
        // TODO get subreddits by post id for better performance
        return subredditRepository.getSubredditsByIds(subredditIds)
            .distinct()
            // Transform the list of subreddits into a map (id -> Subreddit) for faster lookups
            // when joining each post with its subreddit.
            .map { subredditList -> subredditList.associateBy { it.id } }
            .map { subredditMap ->
                cachedPosts.map { post -> subredditMap.getValue(post.subredditId) to post }
            }
            .map { subredditPostList ->
                Listing(
                    subredditPostList,
                    correctedOffset,
                    reachedTheEnd
                )
            }
    }

    private fun loadFromNetwork(
        feed: String,
        pageSize: Int,
        sortType: SortType,
        afterKey: AfterKey,
        offset: Int
    ): Flowable<Listing<Pair<Subreddit, Post>>> {
        return feedRepository.addFeed(feed)
            .andThen(getPostsByFeed(feed, pageSize, afterKey))
            .map { postResponse -> postResponse.getAfterKey() to PostResponseMapper.map(postResponse) }
            .flatMapCompletable { (afterKey, remotePost) ->
                afterKeyRepository.setAfterKey(feed, sortType, afterKey)
                    .andThen(savePost(remotePost, feed, sortType))
            }
            .andThen(execute(feed, sortType, offset, pageSize))
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
                    subredditRepository.addSubreddits(remoteSubreddits)
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
}