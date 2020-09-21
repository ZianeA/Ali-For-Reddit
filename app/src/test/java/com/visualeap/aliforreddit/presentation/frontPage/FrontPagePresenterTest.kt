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
import com.visualeap.aliforreddit.domain.post.Listing
import com.visualeap.aliforreddit.domain.post.Post
import com.visualeap.aliforreddit.domain.subreddit.Subreddit
import com.visualeap.aliforreddit.domain.util.Lce
import com.visualeap.aliforreddit.presentation.frontPage.FrontPageViewState.*
import com.visualeap.aliforreddit.presentation.common.util.ResourceProvider
import com.visualeap.aliforreddit.presentation.frontPage.FrontPageEvent.*
import com.visualeap.aliforreddit.presentation.postDetail.PostDetailEvent
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
import kotlin.math.roundToInt

@RunWith(RobolectricTestRunner::class)
class FrontPagePresenterTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    companion object {
        private const val PAGE_SIZE = 25
        private const val PAGINATION_STEP = PAGE_SIZE / 5
        private const val PAGINATION_POINT_DOWN = PAGE_SIZE / 2f
        private const val PAGINATION_POINT_UP = PAGE_SIZE / 2f - 2
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
                val post = createPost(id = postId, subredditId = "Subreddit1")

                postRepository.addPosts(listOf(post), feed, SortType.Hot).blockingAwait()
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

        presenter = FrontPagePresenter(
            launcher,
            "Feed1",
            fetchFeedPosts,
            ResourceProvider(ApplicationProvider.getApplicationContext()),
            TrampolineSchedulerProvider()
        )
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun `when loading should display progress bar`() {
        val testObserver = presenter.viewState.test()

        presenter.passEvent(ScreenLoadEvent)

        testObserver.assertFirstValue { assertThat(it.loading).isTrue() }
    }

    @Test
    fun `display posts`() {
        val testObserver = presenter.viewState.test()

        presenter.passEvent(ScreenLoadEvent)

        testObserver.assertLastValue { assertThat(it.posts).hasSizeGreaterThan(0) }
    }

    @Test
    fun `display error when fetching posts fails`() {
        //Arrange
        db.postDao().deleteAll().blockingAwait()
        postService.simulateError()
        val testObserver = presenter.viewState.test()

        //Act
        presenter.passEvent(ScreenLoadEvent)

        //Assert
        testObserver.assertLastValue { assertThat(it.error).isNotNull() }
    }

    @Test
    fun `format post`() {
        val testObserver = presenter.viewState.test()

        presenter.passEvent(ScreenLoadEvent)

        testObserver.assertLastValue {
            assertThat(it.posts.first())
                .isEqualToIgnoringGivenFields(
                    createPostDto(), PostDto::id.name, PostDto::subredditId.name
                )
        }
    }

    @Test
    fun `when end is not reached should display progress bar`() {
        val testObserver = presenter.viewState.test()

        presenter.passEvent(ScreenLoadEvent)
        presenter.passEvent(PostBoundEvent(PAGINATION_POINT_DOWN.roundToInt(), true))

        testObserver.assertLastValue { assertThat(it.loadingMore).isTrue() }
    }

    @Test
    fun `when end is reached should hide progress bar`() {
        //Arrange
        db.postDao().deleteAll().blockingAwait()
        val testObserver = presenter.viewState.test()

        //Act
        presenter.passEvent(ScreenLoadEvent)
        presenter.passEvent(PostBoundEvent(PAGINATION_POINT_DOWN.roundToInt(), true))

        //Act and assert
        testObserver.assertLastValue { assertThat(it.loadingMore).isFalse() }
    }

    @Test
    fun `when near the end should load more`() {
        val testObserver = presenter.viewState.test()

        presenter.passEvent(ScreenLoadEvent)
        presenter.passEvent(PostBoundEvent(PAGINATION_POINT_DOWN.roundToInt(), true))

        testObserver.assertLastValue {
            assertThat(it.posts.first().id).isEqualTo("Feed1Post${PAGINATION_STEP + 1}")
        }
    }

    @Test
    fun `when near the top should load more`() {
        //Arrange
        val testObserver = presenter.viewState.test()

        presenter.passEvent(ScreenLoadEvent)
        presenter.passEvent(PostBoundEvent(PAGINATION_POINT_UP.roundToInt(), false))

        testObserver.assertLastValue { assertThat(it.posts.first().id).isEqualTo("Feed1Post1") }
    }

    class FakeFrontPageLauncher : FrontPageLauncher
}