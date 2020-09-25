package com.visualeap.aliforreddit.data.post

import com.visualeap.aliforreddit.data.database.RedditDatabase
import com.visualeap.aliforreddit.data.post.postfeed.PostFeedDao
import com.visualeap.aliforreddit.data.post.postfeed.PostFeedEntity
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

    override fun getPostById(id: String): Observable<Post> = postDao.getById(id)

    override fun getPostsByFeed(
        feed: String,
        sortType: SortType,
        offset: Int,
        limit: Int
    ): Flowable<List<Post>> {
        return postDao.getByFeed(feed, sortType, offset, limit)
    }

    override fun countPostsByFeed(feed: String, sortType: SortType): Single<Int> {
        return Single.fromCallable { postFeedDao.countPostsByFeed(feed, sortType) }
    }

    override fun addPost(post: Post, feed: String, sortType: SortType): Completable {
        return addPosts(listOf(post), feed, sortType)
    }

    override fun addPosts(posts: List<Post>, feed: String, sortType: SortType): Completable {
        return Completable.fromAction {
            db.runInTransaction {
                postDao.addAll(posts)

                val lowestRank = postFeedDao.countPostsByFeed(feed, sortType)
                val postFeedEntities = posts.mapIndexed { index, post ->
                    PostFeedEntity(post.id, feed, sortType, lowestRank + index)
                }
                postFeedDao.addAll(postFeedEntities)
            }
        }
    }

    override fun updatePost(post: Post): Completable {
        return postDao.update(post)
    }
}