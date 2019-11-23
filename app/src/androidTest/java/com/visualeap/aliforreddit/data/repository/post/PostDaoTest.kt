package com.visualeap.aliforreddit.data.repository.post

import androidx.room.Room
import androidx.room.paging.LimitOffsetDataSource
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.visualeap.aliforreddit.data.cache.RedditDatabase
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import util.domain.createPostEntity
import util.domain.createRedditorEntity
import util.domain.createSubredditEntity

@RunWith(AndroidJUnit4::class)
internal class PostDaoTest {
    private lateinit var postDao: PostDao
    private lateinit var db: RedditDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getContext()
        db = Room.inMemoryDatabaseBuilder(context, RedditDatabase::class.java)
            .build()
        postDao = db.postDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertPostListAndGetAll() {
        //Arrange
        val post = createPostEntity()
        val subreddit = createSubredditEntity()

        //Act, Assert
        postDao.addAll(listOf(subreddit), listOf(post))

        val factory = postDao.getAll()
        val posts = (factory.create() as LimitOffsetDataSource).loadRange(0, 1)
        Assert.assertEquals(listOf(PostWithSubredditEntity(post, subreddit)), posts)
    }

    @Test
    fun insertPostAndGetAll() {
        //Arrange
        val post = createPostEntity()
        val subreddit = createSubredditEntity()

        //Act, Assert
        postDao.add(subreddit, post)

        val factory = postDao.getAll()
        val actualPost = (factory.create() as LimitOffsetDataSource)
            .loadRange(0, 1)
            .first()
        val expectedPost = PostWithSubredditEntity(post, subreddit)
        Assert.assertEquals(expectedPost, actualPost)
    }
}