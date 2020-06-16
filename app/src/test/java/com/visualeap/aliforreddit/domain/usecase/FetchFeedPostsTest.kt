package com.visualeap.aliforreddit.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.visualeap.aliforreddit.data.database.RedditDatabase
import com.visualeap.aliforreddit.data.repository.afterkey.DbAfterKeyRepository
import com.visualeap.aliforreddit.data.repository.feed.DbFeedRepository
import com.visualeap.aliforreddit.data.repository.post.DbPostRepository
import com.visualeap.aliforreddit.data.repository.subreddit.DbSubredditRepository
import com.visualeap.aliforreddit.domain.model.AfterKey
import com.visualeap.aliforreddit.domain.model.Post
import com.visualeap.aliforreddit.domain.model.feed.SortType
import com.visualeap.aliforreddit.domain.repository.AfterKeyRepository
import com.visualeap.aliforreddit.domain.repository.FeedRepository
import com.visualeap.aliforreddit.domain.repository.PostRepository
import com.visualeap.aliforreddit.domain.repository.SubredditRepository
import com.visualeap.aliforreddit.util.fake.FakePostWebService
import com.visualeap.aliforreddit.util.fake.FakeSubredditWebService
import org.assertj.core.api.Assertions.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import util.domain.*

@RunWith(RobolectricTestRunner::class)
internal class FetchFeedPostsTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: RedditDatabase
    private lateinit var postRepository: PostRepository
    private lateinit var subredditRepository: SubredditRepository
    private lateinit var afterKeyRepository: AfterKeyRepository
    private lateinit var feedRepository: FeedRepository
    private val postService = FakePostWebService()
    private val subredditService = FakeSubredditWebService()
    private lateinit var fetchFeedPosts: FetchFeedPosts

    @Before
    internal fun setUp() {
        db = createDatabase()
        subredditRepository = DbSubredditRepository(db.subredditDao())
        postRepository = DbPostRepository(db, db.postDao(), db.postFeedDao())
        afterKeyRepository = DbAfterKeyRepository(db.feedAfterKeyDao())
        feedRepository = DbFeedRepository(db.feedDao())

        // Add subreddit to database to satisfy Post foreign key constraint
        val subreddit = createSubreddit()
        subredditRepository.addSubreddit(subreddit).blockingGet()
        subredditService.addSubreddit(subreddit)
        // Add a record to the feed table
        db.feedDao().add(createFeedEntity()).blockingGet()

        fetchFeedPosts =
            FetchFeedPosts(
                postRepository,
                subredditRepository,
                postService,
                subredditService,
                afterKeyRepository,
                feedRepository
            )
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun `return post with its subreddit`() {
        //Arrange
        val subreddits =
            listOf(createSubreddit(id = "FakeSubreddit1"), createSubreddit(id = "FakeSubreddit2"))
        subredditRepository.addSubreddits(subreddits).blockingGet()

        val posts = listOf(
            createPost(id = "1", subredditId = "FakeSubreddit1"),
            createPost(id = "2", subredditId = "FakeSubreddit1"),
            createPost(id = "3", subredditId = "FakeSubreddit2")
        )
        postRepository.addPosts(posts, FEED_NAME, SortType.Best).blockingGet()

        //Act
        val actual = fetchFeedPosts.execute(FEED_NAME, SortType.Best, 0, 10).blockingFirst()

        //Assert
        assertThat(actual.items).containsExactly(
            subreddits[0] to posts[0],
            subreddits[0] to posts[1],
            subreddits[1] to posts[2]
        )
    }

    // TODO Remote
    @Test
    fun `return feed posts by sort type`() {
        //Arrange
        postRepository.addPosts(
            listOf(createPost(id = "1"), createPost(id = "2")), FEED_NAME, SortType.Best
        )
            .blockingGet()

        val posts = listOf(createPost(id = "3"), createPost(id = "4"))
        postRepository.addPosts(posts, FEED_NAME, SortType.Rising).blockingGet()

        //Act
        val actual =
            fetchFeedPosts.execute(FEED_NAME, SortType.Rising, 0, 10).blockingFirst()

        //Assert
        assertThat(actual.items).extracting<Post> { it.second }
            .containsExactlyElementsOf(posts)
    }

    @Test
    fun `should offset posts`() {
        //Arrange
        val post = createPost(id = "2")
        postRepository.addPosts(listOf(createPost(id = "1"), post), FEED_NAME, SortType.Best)
            .blockingGet()

        //Act
        val actual =
            fetchFeedPosts.execute(FEED_NAME, SortType.Best, 1, 1).blockingFirst()

        //Assert
        assertThat(actual.items).extracting<Post> { it.second }
            .containsExactly(post)
    }

    @Test
    fun `should return offset`() {
        //Arrange
        postRepository.addPosts(
            listOf(createPost(id = "1"), createPost(id = "2")),
            FEED_NAME,
            SortType.Best
        )
            .blockingGet()

        //Act
        val actual =
            fetchFeedPosts.execute(FEED_NAME, SortType.Best, 1, 1).blockingFirst()

        //Assert
        assertThat(actual.offset).isEqualTo(1)
    }

    @Test
    fun `when offset is out of range should correct it`() {
        //Arrange
        val posts = listOf(createPost(id = "1"), createPost(id = "2"))
        postRepository.addPosts(
            posts,
            FEED_NAME,
            SortType.Best
        )
            .blockingGet()

        //Act
        val actual =
            fetchFeedPosts.execute(FEED_NAME, SortType.Best, 3, 2).blockingFirst()

        //Assert
        assertThat(actual.items).extracting<Post> { it.second }
            .containsExactlyElementsOf(posts)
    }

    @Test
    fun `when offset doesn't allow the required page size to be reached, should correct it`() {
        //Arrange
        val posts = listOf(createPost(id = "1"), createPost(id = "2"), createPost(id = "3"))
        postRepository.addPosts(
            posts,
            FEED_NAME,
            SortType.Best
        )
            .blockingGet()

        //Act
        val actual =
            fetchFeedPosts.execute(FEED_NAME, SortType.Best, 1, 3).blockingFirst()

        //Assert
        assertThat(actual.items).extracting<Post> { it.second }
            .containsExactlyElementsOf(posts)
    }

    @Test
    fun `when offset is smaller than zero should correct it`() {
        //Arrange
        val posts = listOf(createPost(id = "1"), createPost(id = "2"), createPost(id = "3"))
        postRepository.addPosts(
            posts,
            FEED_NAME,
            SortType.Best
        )
            .blockingGet()

        //Act
        val actual =
            fetchFeedPosts.execute(FEED_NAME, SortType.Best, -5, 3).blockingFirst()

        //Assert
        assertThat(actual.items).extracting<Post> { it.second }
            .containsExactlyElementsOf(posts)
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
        val actual = fetchFeedPosts.execute(FEED_NAME, SortType.Best, 0, 1).blockingFirst()

        //Assert
        assertThat(actual.items).hasSize(1)
    }

    @Test
    fun `when cache is empty should fetch posts`() {
        //Arrange
        val subreddit = createSubreddit(id = "RemoteSubreddit")
        subredditService.addSubreddit(subreddit)
        val post = createPost(id = "1", title = "Remote Post", subredditId = subreddit.id)
        postService.addPost(FEED_NAME, post)

        //Act, assert
        fetchFeedPosts.execute(FEED_NAME, SortType.Best, 0, 1)
            .test()
            .assertValueAt(1, match { assertThat(it.items).containsExactly(subreddit to post) })
    }

    @Test
    fun `when the requested page size is greater than the number of cached items, should load more`() {
        //Arrange
        val localPost = createPost(id = "1", title = "Local Post")
        postRepository.addPosts(listOf(localPost), FEED_NAME, SortType.Best).blockingGet()

        val remotePost = createPost(id = "2", title = "Remote Post")
        postService.addPost(FEED_NAME, remotePost)

        //Act and assert
        fetchFeedPosts.execute(FEED_NAME, SortType.Best, 0, 2)
            .test()
            .assertValueAt(1, match { listing ->
                assertThat(listing.items).extracting<Post> { it.second }
                    .containsExactly(localPost, remotePost)
            })
    }

    @Test
    fun `return cached items while loading more`() {
        //Arrange
        val localPost = createPost(id = "1", title = "Local Post")
        postRepository.addPosts(listOf(localPost), FEED_NAME, SortType.Best).blockingGet()

        val remotePost = createPost(id = "2", title = "Remote Post")
        postService.addPost(FEED_NAME, remotePost)

        //Act and assert
        fetchFeedPosts.execute(FEED_NAME, SortType.Best, 0, 2)
            .test()
            .assertValueAt(0, match { listing ->
                assertThat(listing.items).extracting<Post> { it.second }
                    .containsExactly(localPost)
            })
            .assertValueAt(1, match { listing ->
                assertThat(listing.items).extracting<Post> { it.second }
                    .containsExactly(localPost, remotePost)
            })
    }

    @Test
    fun `when the end of the cached items is reached, should load more`() {
        //Arrange
        afterKeyRepository.setAfterKey(FEED_NAME, SortType.Best, AfterKey.Next("1")).blockingGet()

        val remotePost = createPost(id = "2", title = "Next Remote Post")
        postService.addPosts(
            FEED_NAME,
            listOf(createPost(id = "1", title = "Remote"), remotePost)
        )

        postRepository.addPosts(listOf(createPost(id = "1")), FEED_NAME, SortType.Best)
            .blockingGet()

        //Act and assert
        fetchFeedPosts.execute(FEED_NAME, SortType.Best, 1, 1)
            .test()
            .assertValueAt(1, match { listing ->
                assertThat(listing.items).extracting<Post> { it.second }
                    .containsExactly(remotePost)
            })
    }

    @Test
    fun `when the end of the remote items is not reached, reachedTheEnd should be false`() {
        //Arrange
        postService.addPosts(FEED_NAME, listOf(createPost(id = "1"), createPost(id = "2")))

        //Act
        fetchFeedPosts.execute(FEED_NAME, SortType.Best, 0, 1)
            .test()
            .assertValueAt(1, match { listing -> assertThat(listing.reachedTheEnd).isFalse() })
    }

    @Test
    fun `when the end of the remote items is reached, reachedTheEnd should be true`() {
        //Act
        fetchFeedPosts.execute(FEED_NAME, SortType.Best, 0, 2)
            .test()
            .assertValueAt(1, match { listing -> assertThat(listing.reachedTheEnd).isTrue() })
    }

    @Test
    fun `return posts by feed`() {
        //Arrange
        postService.addPosts(FEED_NAME, listOf(createPost(id = "1"), createPost(id = "2")))

        val feedPosts = listOf(createPost(id = "3"), createPost(id = "4"))
        postService.addPosts("FakeFeed2", feedPosts)

        //Act and assert
        fetchFeedPosts.execute("FakeFeed2", SortType.Best, 0, 2)
            .test()
            .assertValueAt(1, match {
                assertThat(it.items).extracting<Post> { it.second }
                    .containsExactlyElementsOf(feedPosts)
            })
    }
}