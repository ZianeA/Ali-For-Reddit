package com.visualeap.aliforreddit.data.post

import com.visualeap.aliforreddit.data.database.RedditDatabase
import com.visualeap.aliforreddit.domain.feed.SortType
import com.visualeap.aliforreddit.domain.post.PostRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import util.domain.*

@RunWith(RobolectricTestRunner::class)
internal class PostRoomRepositoryTest {
    private lateinit var db: RedditDatabase
    private lateinit var postRepository: PostRepository

    @Before
    fun setUp() {
        db = createDatabase()

        // Add subreddit to database to satisfy Post foreign key constraint
        db.subredditDao().add(createSubredditEntity()).blockingGet()

        // Add a record to the feed table
        db.feedDao().add(createFeedEntity()).blockingGet()

        postRepository = PostRoomRepository(db, db.postDao(), db.postFeedDao())
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun `add feed posts to database`() {
        //Act
        val posts =
            listOf(createPost(id = "1"), createPost(id = "2"), createPost(id = "3"))
        postRepository.addPosts(posts, FEED_NAME, SortType.Best)
            .test()
            .assertResult()

        //Assert
        val actual = postRepository.getPostsByFeed(FEED_NAME, SortType.Best, 0, 10).blockingFirst()
        assertThat(actual).containsExactlyInAnyOrderElementsOf(posts)
    }

    @Test
    fun `rank posts in the feed`() {
        //Act
        val posts = listOf(
            createPost(id = "1"),
            createPost(id = "2"),
            createPost(id = "3"),
            createPost(id = "4")
        )
        postRepository.addPosts(listOf(posts[0], posts[1]), FEED_NAME, SortType.Best).blockingGet()
        postRepository.addPosts(listOf(posts[2]), FEED_NAME, SortType.Best).blockingGet()
        postRepository.addPosts(listOf(posts[3]), FEED_NAME, SortType.Best).blockingGet()

        //Assert
        val actual = postRepository.getPostsByFeed(FEED_NAME, SortType.Best, 3, 10).blockingFirst()
        assertThat(actual).containsExactly(posts[3])
    }

    @Test
    fun `return posts by feed`() {
        //Arrange
        db.feedDao().add(createFeedEntity("FakeFeed2")).blockingGet()

        postRepository.addPosts(
            listOf(createPost(id = "1"), createPost(id = "2")), "FakeFeed2", SortType.Best
        )
            .blockingGet()

        val posts = listOf(createPost(id = "3"), createPost(id = "4"))
        postRepository.addPosts(
            posts, FEED_NAME, SortType.Best
        )
            .blockingGet()

        //Act
        val actual = postRepository.getPostsByFeed(FEED_NAME, SortType.Best, 0, 10).blockingFirst()

        //Assert
        assertThat(actual).containsExactlyElementsOf(posts)
    }

    @Test
    fun `return feed posts by sort type`() {
        //Arrange
        postRepository.addPosts(
            listOf(createPost(id = "1"), createPost(id = "2")), FEED_NAME, SortType.Best
        )
            .blockingGet()

        val posts = listOf(createPost(id = "3"), createPost(id = "4"))
        postRepository.addPosts(
            posts, FEED_NAME, SortType.Rising
        )
            .blockingGet()

        //Act
        val actual =
            postRepository.getPostsByFeed(FEED_NAME, SortType.Rising, 0, 10).blockingFirst()

        //Assert
        assertThat(actual).containsExactlyElementsOf(posts)
    }

    @Test
    fun `should offset posts`() {
        //Arrange
        val post = createPost(id = "2")
        postRepository.addPosts(
            listOf(createPost(id = "1"), post), FEED_NAME, SortType.Best
        )
            .blockingGet()

        //Act
        val actual =
            postRepository.getPostsByFeed(FEED_NAME, SortType.Best, 1, 10).blockingFirst()

        //Assert
        assertThat(actual).containsExactly(post)
    }

    @Test
    fun `limit number of posts returned`() {
        //Arrange
        postRepository.addPosts(
            listOf(createPost(id = "1"), createPost(id = "2"), createPost(id = "3")),
            FEED_NAME,
            SortType.Best
        )
            .blockingGet()

        //Act
        val actual = postRepository.getPostsByFeed(FEED_NAME, SortType.Best, 0, 1).blockingFirst()

        //Assert
        assertThat(actual).hasSize(1)
    }

    @Test
    fun `should return number of posts in a feed`() {
        //Arrange
        postRepository.addPosts(
            listOf(createPost(id = "1"), createPost(id = "2"), createPost(id = "3")),
            FEED_NAME,
            SortType.Best
        )
            .blockingGet()

        //Act
        val count = postRepository.countPostsByFeed(FEED_NAME, SortType.Best).blockingGet()

        //Assert
        assertThat(count).isEqualTo(3)
    }
}
