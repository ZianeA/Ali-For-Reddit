package com.visualeap.aliforreddit.presentation.frontPage

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.visualeap.aliforreddit.util.TrampolineSchedulerProvider
import com.visualeap.aliforreddit.data.database.RedditDatabase
import com.visualeap.aliforreddit.data.afterkey.AfterKeyRoomRepository
import com.visualeap.aliforreddit.data.feed.FeedRoomRepository
import com.visualeap.aliforreddit.data.post.PostRoomRepository
import com.visualeap.aliforreddit.data.subreddit.SubredditRoomRepository
import com.visualeap.aliforreddit.domain.feed.SortType
import com.visualeap.aliforreddit.domain.post.FetchFeedPosts
import com.visualeap.aliforreddit.presentation.frontPage.FrontPageViewState.*
import com.visualeap.aliforreddit.presentation.common.util.ResourceProvider
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
class FrontPagePresenterTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    companion object {
        private const val PAGE_SIZE = 25
        private const val PAGINATION_STEP: Int = PAGE_SIZE / 4
        private const val DATABASE_POST_COUNT = 50
    }

    private val launcher = FakeFrontPageLauncher()
    private lateinit var db: RedditDatabase
    private lateinit var fetchFeedPosts: FetchFeedPosts
    private val postService = FakePostWebService()
    private lateinit var presenter: FrontPagePresenter

    @Before
    internal fun setUp() {
        db = createDatabase()

        // TODO clean up
        val subredditRepository = SubredditRoomRepository(db.subredditDao())
        subredditRepository.addSubreddit(createSubreddit(id = "Subreddit1")).blockingAwait()
        subredditRepository.addSubreddit(createSubreddit(id = "Subreddit2")).blockingAwait()

        val feedRepository = FeedRoomRepository(db.feedDao())
        val feeds = listOf("Feed1", "Feed2")
        feeds.forEach {
            feedRepository.addFeed(it).blockingAwait()
        }

        val postRepository = PostRoomRepository(db, db.postDao(), db.postFeedDao())
        for (i in 1..DATABASE_POST_COUNT + 1) {
            feeds.forEach { feed ->
                // example: feed1Post1
                val postId = "${feed}Post$i"

                postRepository.addPosts(
                    listOf(createPost(id = postId, subredditId = "Subreddit1")), feed, SortType.Hot
                )
                    .blockingAwait()
            }
        }

        fetchFeedPosts = FetchFeedPosts(
            postRepository,
            subredditRepository,
            postService,
            FakeSubredditWebService(),
            AfterKeyRoomRepository(db.feedAfterKeyDao()),
            feedRepository
        )

        val context = ApplicationProvider.getApplicationContext<Application>()
        presenter = FrontPagePresenter(
            launcher,
            "Feed1",
            fetchFeedPosts,
            ResourceProvider(context),
            TrampolineSchedulerProvider()
        )
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun `display posts`() {
        //Act and assert
        presenter.start()
            .test()
            .assertValueAt(1, match { frontPageViewState ->
                assertThat(frontPageViewState).isInstanceOf(Success::class.java)
                    .extracting { (it as Success).posts }.asList()
                    .hasSize(PAGE_SIZE)
            })
    }

    @Test
    fun `format post`() {
        //Act and assert
        presenter.start()
            .test()
            .assertValueAt(1, match {
                val firstPost = (it as Success).posts.first()
                assertThat(firstPost).isEqualToIgnoringGivenFields(
                    createFeedPostDto(), FeedPostDto::id.name, FeedPostDto::subredditId.name
                )
            })
    }

    @Test
    fun `when end is not reached should display loading`() {
        //Act and assert
        presenter.start()
            .test()
            .assertValueAt(1, match { assertThat((it as Success).isLoading).isEqualTo(true) })
    }

    @Test
    fun `when end is reached should hide loading`() {
        //Arrange
        db.postDao().deleteAll().blockingAwait()

        //Act and assert
        presenter.start()
            .test()
            .assertValueAt(1, match { assertThat((it as Success).isLoading).isEqualTo(false) })
    }

    @Test
    fun `when near the end should load more`() {
        //Arrange
        val paginationPoint = PAGE_SIZE * 3 / 4 + 1
        presenter.onPostBound(paginationPoint)

        //Act and assert
        presenter.start()
            .test()
            .assertValueAt(1, match { viewState ->
                val firstPost = (viewState as Success).posts.first()
                assertThat(firstPost)
                    .extracting { it.id }
                    .isEqualTo("Feed1Post${PAGINATION_STEP + 1}")
            })
    }

    @Test
    fun `when near the top should load more`() {
        //Arrange
        presenter.onPostBound(PAGE_SIZE)
        presenter.onPostBound(0)

        //Act and assert
        presenter.start()
            .test()
            .assertValueAt(1, match { viewState ->
                val firstPost = (viewState as Success).posts.first()
                assertThat(firstPost)
                    .extracting { it.id }
                    .isEqualTo("Feed1Post1")
            })
    }

    @Test
    fun `display error when fetching posts fails`() {
        //Arrange
        db.postDao().deleteAll().blockingAwait()
        postService.simulateError()

        //Act and assert
        presenter.start()
            .test()
            .assertValueAt(1, match { assertThat(it).isInstanceOf(Failure::class.java) })
    }

    class FakeFrontPageLauncher : FrontPageLauncher
}