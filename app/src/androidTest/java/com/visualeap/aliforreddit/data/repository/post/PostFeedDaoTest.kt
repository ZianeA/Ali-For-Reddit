package com.visualeap.aliforreddit.data.repository.post

import androidx.room.Room
import androidx.room.paging.LimitOffsetDataSource
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.visualeap.aliforreddit.data.cache.RedditDatabase
import com.visualeap.aliforreddit.data.repository.feed.DefaultFeed
import com.visualeap.aliforreddit.data.repository.feed.FeedDao
import com.visualeap.aliforreddit.data.repository.feed.FeedEntity
import com.visualeap.aliforreddit.data.repository.post.postfeed.PostFeedDao
import com.visualeap.aliforreddit.data.repository.post.postfeed.PostFeedEntity
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import util.domain.*

@RunWith(AndroidJUnit4::class)
class PostFeedDaoTest {
    private lateinit var postDao: PostDao
    private lateinit var feedDao: FeedDao
    private lateinit var postFeedDao: PostFeedDao
    private lateinit var db: RedditDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().context
        db = Room.inMemoryDatabaseBuilder(context, RedditDatabase::class.java)
            .build()
        postDao = db.postDao()
        feedDao = db.feedDao()
        postFeedDao = db.postFeedDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAndGet() {
        //Arrange
        val post = createPostEntity()
        postDao.addAll(listOf(createSubredditEntity()), listOf(post))
        feedDao.add(FeedEntity(DefaultFeed.Home.name)).test()

        val postFeed = PostFeedEntity(post.id, DefaultFeed.Home.name, 0)
        postFeedDao.add(postFeed).test()

        //Act, assert
        postFeedDao.getAll()
            .test()
            .assertResult(listOf(postFeed))
    }

    @Test
    fun getPostsByFeed() {
        //Arrange
        val homePost = createPostEntity()
        val popularPost = createPostEntity(id = "909")
        val secondHomePost = createPostEntity(id = "505")
        postDao.addAll(
            listOf(createSubredditEntity()),
            listOf(homePost, popularPost, secondHomePost)
        )
        feedDao.addAll(
            listOf(
                FeedEntity(DefaultFeed.Home.name),
                FeedEntity(DefaultFeed.Popular.name)
            )
        ).test()
        postFeedDao.addAll(
            listOf(
                PostFeedEntity(homePost.id, DefaultFeed.Home.name, 0),
                PostFeedEntity(popularPost.id, DefaultFeed.Popular.name, 1),
                PostFeedEntity(secondHomePost.id, DefaultFeed.Home.name, 2)
            )
        ).test()

        //Act
        val factory = postFeedDao.getPostsForFeed(DefaultFeed.Home.name)
        val posts = (factory.create() as LimitOffsetDataSource).loadRange(0, 10)

        //Assert
        Assert.assertEquals(
            listOf(
                createPostWithSubreddit(homePost),
                createPostWithSubreddit(secondHomePost)
            ), posts
        )
    }

    @Test
    fun getPostsByFeedShouldIgnoreCasing() {
        //Arrange
        val homePost = createPostEntity()
        val popularPost = createPostEntity(id = "909")
        postDao.addAll(
            listOf(createSubredditEntity()),
            listOf(homePost, popularPost)
        )
        feedDao.addAll(
            listOf(
                FeedEntity(DefaultFeed.Home.name),
                FeedEntity(DefaultFeed.Popular.name)
            )
        ).test()
        postFeedDao.addAll(
            listOf(
                PostFeedEntity(homePost.id, DefaultFeed.Home.name, 0),
                PostFeedEntity(popularPost.id, DefaultFeed.Popular.name, 1)
            )
        ).test()

        //Act
        val factory = postFeedDao.getPostsForFeed("HoMe")
        val posts = (factory.create() as LimitOffsetDataSource).loadRange(0, 10)

        //Assert
        Assert.assertEquals(listOf(createPostWithSubreddit(homePost)), posts)
    }
}