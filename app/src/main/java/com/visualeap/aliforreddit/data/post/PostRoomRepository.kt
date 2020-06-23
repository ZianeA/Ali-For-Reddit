package com.visualeap.aliforreddit.data.post

import com.visualeap.aliforreddit.data.database.RedditDatabase
import com.visualeap.aliforreddit.data.post.postfeed.PostFeedDao
import com.visualeap.aliforreddit.data.post.postfeed.PostFeedEntity
import com.visualeap.aliforreddit.domain.post.Post
import com.visualeap.aliforreddit.domain.feed.SortType
import com.visualeap.aliforreddit.domain.post.PostRepository
import dagger.Reusable
import io.reactivex.*
import javax.inject.Inject

@Reusable
class PostRoomRepository @Inject constructor(
    private val db: RedditDatabase,
    private val postDao: PostDao,
    private val postFeedDao: PostFeedDao
) : PostRepository {

    override fun getPostsByFeed(
        feed: String,
        sortType: SortType,
        offset: Int,
        limit: Int
    ): Flowable<List<Post>> {
        return postDao.getByFeed(feed, sortType, offset, limit)
            .map { postList -> postList.map(::toDomain) }
    }

    override fun countPostsByFeed(feed: String, sortType: SortType): Single<Int> {
        return Single.fromCallable { postFeedDao.countPostsByFeed(feed, sortType) }
    }

    override fun addPosts(posts: List<Post>, feed: String, sortType: SortType): Completable {
        return Completable.fromAction {
            db.runInTransaction {
                postDao.addAll(posts.map(::toEntity))

                val lowestRank = postFeedDao.countPostsByFeed(feed, sortType)
                val postFeedEntities = posts.mapIndexed { index, post ->
                    PostFeedEntity(post.id, feed, sortType, lowestRank + index)
                }
                postFeedDao.addAll(postFeedEntities)
            }
        }
    }

    private fun toEntity(post: Post): PostEntity {
        return post.run {
            PostEntity(id, authorName, title, text, score, commentCount, subredditId, created)
        }
    }

    private fun toDomain(post: PostEntity): Post {
        return post.run {
            Post(
                id,
                authorName,
                title,
                text,
                score,
                commentCount,
                subredditId,
                created
            )
        }
    }
}