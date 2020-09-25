package com.visualeap.aliforreddit.domain.post

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.visualeap.aliforreddit.data.database.RedditDatabase
import com.visualeap.aliforreddit.data.post.Post
import com.visualeap.aliforreddit.data.post.PostRoomRepository
import com.visualeap.aliforreddit.data.subreddit.Subreddit
import com.visualeap.aliforreddit.domain.feed.SortType
import com.visualeap.aliforreddit.domain.util.Lce
import com.visualeap.aliforreddit.util.fake.FakePostWebService
import org.assertj.core.api.AbstractObjectAssert
import org.assertj.core.api.Assertions.*
import org.assertj.core.api.InstanceOfAssertFactories
import org.assertj.core.api.ObjectAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import util.domain.*

@RunWith(RobolectricTestRunner::class)
internal class GetPostByIdTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: RedditDatabase
    private lateinit var postRepository: PostRepository
    private lateinit var postWebService: FakePostWebService
    private lateinit var getPostById: GetPostById

    @Before
    internal fun setUp() {
        db = createDatabase()
        db.feedDao().add(createFeedEntity()).blockingAwait()
        postRepository = PostRoomRepository(db, db.postDao(), db.postFeedDao())
        postWebService = FakePostWebService()
        getPostById =
            GetPostById(postRepository, db.subredditDao(), postWebService)

        // Add two subreddits with two posts
        for (i in 1..2) {
            val subredditId = "Subreddit$i"
            db.subredditDao().add(createSubreddit(id = subredditId)).blockingAwait()

            for (j in 1..2) {
                val postId = "${subredditId}Post$j"
                postWebService.addPost(
                    subredditId,
                    createPost(id = postId, text = "Remote", subredditId = subredditId)
                )

                postRepository.addPosts(
                    listOf(createPost(id = postId, text = "Cache", subredditId = subredditId)),
                    FEED_NAME,
                    SortType.Best
                ).blockingAwait()
            }
        }
    }

    @After
    internal fun tearDown() {
        db.close()
    }

    @Test
    fun `return post by id`() {
        //Act and assert
        getPostById.execute("Subreddit2Post2")
            .test()
            .assertLastValue { lce ->
                assertThat(lce).extractingPair()
                    .satisfies {
                        assertThat(it.first).extracting(Subreddit::id).isEqualTo("Subreddit2")
                        assertThat(it.second).extracting(Post::id).isEqualTo("Subreddit2Post2")
                    }
            }
    }

    @Test
    fun `return cached post first`() {
        //Act and assert
        getPostById.execute("Subreddit1Post1")
            .filter { it is Lce.Content }
            .test()
            .assertValueAt(
                0,
                match { lce ->
                    assertThat(lce).extractingPost().extracting(Post::text).isEqualTo("Cache")
                }
            )
    }

    @Test
    fun `return remote post second`() {
        //Act and assert
        getPostById.execute("Subreddit1Post1")
            .filter { it is Lce.Content }
            .test()
            .assertValueAt(
                1,
                match { lce ->
                    assertThat(lce).extractingPost().extracting(Post::text).isEqualTo("Remote")
                }
            )
    }

    @Test
    fun `should not return remote post if it's the same as cached post`() {
        // Arrange
        // This will update the cache with latest data
        getPostById.execute("Subreddit1Post1").test()

        //Act
        getPostById.execute("Subreddit1Post1")
            .filter { it is Lce.Content }
            .test()
            .assertValueCount(1)
    }

    @Test
    fun `when remote post fetching fails return error`() {
        //Arrange
        postWebService.simulateError()

        //Act
        getPostById.execute("Subreddit1Post1")
            .test()
            .assertLastValue { lce -> assertThat(lce).isInstanceOf(Lce.Error::class.java) }
    }

    private fun <T> ObjectAssert<T>.extractingPair(): AbstractObjectAssert<*, Pair<Subreddit, Post>> {
        return this.asInstanceOf(InstanceOfAssertFactories.type(Lce.Content::class.java))
            .extracting { it.data as Pair<Subreddit, Post> }
    }

    private fun <T> ObjectAssert<T>.extractingPost(): AbstractObjectAssert<*, Post> {
        return this.extractingPair().extracting { it.second }
    }

    private fun <T> ObjectAssert<T>.extractingSubreddit(): AbstractObjectAssert<*, Subreddit> {
        return this.extractingPair().extracting { it.first }
    }
}