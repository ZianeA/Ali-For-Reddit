package com.visualeap.aliforreddit.domain.comment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.visualeap.aliforreddit.data.comment.CommentRoomRepository
import com.visualeap.aliforreddit.data.database.RedditDatabase
import com.visualeap.aliforreddit.data.subreddit.SubredditRoomRepository
import com.visualeap.aliforreddit.domain.util.Lce
import com.visualeap.aliforreddit.util.fake.FakeCommentWebService
import org.assertj.core.api.Assertions.*
import org.assertj.core.api.InstanceOfAssertFactories
import org.assertj.core.api.ListAssert
import org.assertj.core.api.ObjectAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import util.domain.*

@RunWith(RobolectricTestRunner::class)
class GetCommentsByPostTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: RedditDatabase
    private lateinit var commentRepository: CommentRepository
    private lateinit var commentService: FakeCommentWebService
    private lateinit var getCommentsByPost: GetCommentsByPost

    @Before
    internal fun setUp() {
        db = createDatabase()
        db.feedDao().add(createFeedEntity()).blockingAwait()

        val subredditRepository = SubredditRoomRepository(db.subredditDao())
        commentRepository = CommentRoomRepository(db.commentDao())
        commentService = FakeCommentWebService()

        // Add two posts with two comments
        for (i in 1..2) {
            val subredditId = "Subreddit$i"
            subredditRepository.addSubreddit(createSubreddit(name = subredditId, id = subredditId))
                .blockingAwait()

            val postId = "Post$i"
            db.postDao().add(createPostEntity(id = postId, subredditId = subredditId))
                .blockingAwait()

            for (j in 1..2) {
                commentRepository.addComment(
                    createCommentB(id = "${postId}Comment$j", text = "Cache", postId = postId)
                ).blockingAwait()
                commentService.addComment(
                    subredditId,
                    createCommentB(id = "${postId}Comment$j", text = "Remote", postId = postId)
                )
            }
        }

        getCommentsByPost =
            GetCommentsByPost(commentRepository, commentService)
    }

    @After
    internal fun tearDown() {
        db.close()
    }

    @Test
    fun `return loading`() {
        //Act and assert
        getCommentsByPost.execute("Post2", "Subreddit2")
            .test()
            .assertValueAt(0, match { lce ->
                assertThat(lce).isInstanceOf(Lce.Loading::class.java)
            })
    }

    @Test
    fun `return comments by post`() {
        //Act and assert
        getCommentsByPost.execute("Post2", "Subreddit2")
            .test()
            .assertLastValue { lce ->
                assertThat(lce).extractingComments()
                    .usingElementComparatorIgnoringFields(Comment::text.name)
                    .containsExactly(
                        createCommentB(id = "Post2Comment1", postId = "Post2"),
                        createCommentB(id = "Post2Comment2", postId = "Post2")
                    )
            }
    }

    @Test
    fun `return cached comments first`() {
        //Act
        getCommentsByPost.execute("Post1", "Subreddit1")
            .filter { it is Lce.Content }
            .test()
            .assertValueAt(0, match { lce ->
                assertThat(lce).extractingComments()
                    .extracting(Comment::text.name)
                    .hasSizeGreaterThan(0)
                    .allSatisfy { assertThat(it).isEqualTo("Cache") }
            })
    }

    @Test
    fun `return remote comments second`() {
        //Act
        getCommentsByPost.execute("Post1", "Subreddit1")
            .test()
            .assertLastValue { comments ->
                assertThat(comments).extractingComments()
                    .extracting(Comment::text.name)
                    .hasSizeGreaterThan(0)
                    .anySatisfy { assertThat(it).isEqualTo("Remote") }
            }
    }

    @Test
    fun `when remote data is the same as cache data should not return it`() {
        // Arrange
        // This will update the cache with latest data
        getCommentsByPost.execute("Post1", "Subreddit1").test()

        //Act
        getCommentsByPost.execute("Post1", "Subreddit1")
            .filter { it is Lce.Content }
            .test()
            .assertValueCount(1)
    }

    @Test
    fun `when cache is empty but remote is not should not return empty list`() {
        //Arrange
        clearCache()

        //Act
        getCommentsByPost.execute("Post1", "Subreddit1")
            .test()
            .assertLastValue { lce -> assertThat(lce).extractingComments().hasSizeGreaterThan(0) }
    }

    @Test
    fun `when remote comments fetching fails return error`() {
        //Arrange
        commentService.simulateError()

        //Act
        getCommentsByPost.execute("Post1", "Subreddit1")
            .test()
            .assertNotTerminated()
            .assertLastValue { lce -> assertThat(lce).isInstanceOf(Lce.Error::class.java) }
    }

    @Test
    fun `when there are no comments return an empty list`() {
        //Arrange
        clearCache()
        commentService.deleteAll()

        //Act
        getCommentsByPost.execute("Post1", "Subreddit1")
            .filter { it is Lce.Content }
            .test()
            .assertLastValue { lce -> assertThat(lce).extractingComments().isEmpty() }
    }

    private fun clearCache() = db.commentDao().deleteAll().blockingAwait()

    private fun <T> ObjectAssert<T>.extractingComments(): ListAssert<Comment> {
        return this.asInstanceOf(InstanceOfAssertFactories.type(Lce.Content::class.java))
            .extracting { it.data }
            .asInstanceOf(InstanceOfAssertFactories.list(Comment::class.java))
    }
}