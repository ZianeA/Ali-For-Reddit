package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.data.repository.post.PostResponse
import com.visualeap.aliforreddit.data.repository.post.PostWebService
import com.visualeap.aliforreddit.data.repository.subreddit.SubredditResponse
import com.visualeap.aliforreddit.data.repository.subreddit.SubredditWebService
import com.visualeap.aliforreddit.domain.model.AfterKey
import com.visualeap.aliforreddit.domain.model.Listing
import com.visualeap.aliforreddit.domain.model.Post
import com.visualeap.aliforreddit.domain.model.Subreddit
import com.visualeap.aliforreddit.domain.model.feed.SortType
import com.visualeap.aliforreddit.domain.repository.AfterKeyRepository
import com.visualeap.aliforreddit.domain.repository.FeedRepository
import com.visualeap.aliforreddit.domain.repository.PostRepository
import com.visualeap.aliforreddit.domain.repository.SubredditRepository
import dagger.Reusable
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.internal.operators.completable.CompletableDefer
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
                                    // Return cached items first (first observable) while fetching remote items (second observable)
                                    Flowable.merge(
                                        loadFromCache(cachedPosts, endOfRemote, correctedOffset),
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
        return subredditRepository.getSubredditsByIds(subredditIds)
            .distinct()
            // Transform the list of subreddits into a map (id -> Subreddit) for faster lookups
            // when joining each post with its subreddit.
            .map { subredditList -> subredditList.associateBy { it.id } }
            .map { subredditMap ->
                cachedPosts.map { post -> subredditMap.getValue(post.subredditId) to post }
            }
            .map { subredditPostList -> Listing(subredditPostList, correctedOffset, reachedTheEnd) }
    }

    private fun loadFromNetwork(
        feed: String,
        pageSize: Int,
        sortType: SortType,
        afterKey: AfterKey,
        offset: Int
    ): Flowable<Listing<Pair<Subreddit, Post>>> {
        return feedRepository.addFeed(feed)
            .andThen(postService.getPostsBySubreddit(feed, pageSize, afterKey.toStringOrNull()))
            .map { postResponse -> postResponse.getAfterKey() to postResponse.toDomain() }
            .flatMapCompletable { (afterKey, remotePost) ->
                afterKeyRepository.setAfterKey(feed, sortType, afterKey)
                    .andThen(savePost(remotePost, feed, sortType))
            }
            .andThen(execute(feed, sortType, offset, pageSize))
    }

    private fun savePost(
        remotePost: List<Post>,
        feed: String,
        sortType: SortType
    ): Completable {
        return if (remotePost.isNotEmpty()) {
            // For each post, fetch the subreddit it belongs to
            val subredditIds = remotePost.joinToString { it.subredditId }
            subredditService.getSubreddits(subredditIds)
                .map { subredditResponse -> subredditResponse.toDomain() }
                .flatMapCompletable { remoteSubreddits ->
                    // The subreddit must be added first because of the foreign key constraint
                    subredditRepository.addSubreddits(remoteSubreddits)
                        .andThen(postRepository.addPosts(remotePost, feed, sortType))
                }
        } else Completable.complete()
    }

    private fun PostResponse.toDomain(): List<Post> {
        return this.data.postHolders.map { holder ->
            holder.post.run {
                Post(id, authorName, title, text, score, commentCount, subredditId, created)
            }
        }
    }

    private fun SubredditResponse.toDomain(): List<Subreddit> {
        return this.data.subredditHolders.map { holder ->
            holder.subreddit.run { Subreddit(id, name, iconUrl, primaryColor, keyColor) }
        }
    }

    private fun PostResponse.getAfterKey(): AfterKey {
        return when (val key = this.data.afterKey) {
            null -> AfterKey.End
            else -> AfterKey.Next(key)
        }
    }
}