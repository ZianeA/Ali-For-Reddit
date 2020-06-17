package com.visualeap.aliforreddit.presentation.main.frontPage

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.visualeap.aliforreddit.util.TrampolineSchedulerProvider
import com.visualeap.aliforreddit.data.database.RedditDatabase
import com.visualeap.aliforreddit.data.repository.afterkey.DbAfterKeyRepository
import com.visualeap.aliforreddit.data.repository.feed.DbFeedRepository
import com.visualeap.aliforreddit.data.repository.post.DbPostRepository
import com.visualeap.aliforreddit.data.repository.subreddit.DbSubredditRepository
import com.visualeap.aliforreddit.domain.model.feed.SortType
import com.visualeap.aliforreddit.domain.usecase.FetchFeedPosts
import com.visualeap.aliforreddit.presentation.util.ResourceProvider
import com.visualeap.aliforreddit.util.fake.FakePostWebService
import com.visualeap.aliforreddit.util.fake.FakeSubredditWebService
import org.assertj.core.api.Assertions.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import util.domain.createDatabase
import util.domain.createFeedPostDto
import util.domain.createPost
import util.domain.createSubreddit

@RunWith(RobolectricTestRunner::class)
class FrontPagePresenterTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    companion object {
        private const val PAGE_SIZE = 25
        private const val PAGINATION_STEP: Int = PAGE_SIZE / 4
    }

    private val view = FakeFrontPageView()
    private lateinit var db: RedditDatabase
    private lateinit var fetchFeedPosts: FetchFeedPosts
    private lateinit var presenter: FrontPagePresenter

    @Before
    internal fun setUp() {
        db = createDatabase()

        // TODO clean up
        val subredditRepository = DbSubredditRepository(db.subredditDao())
        subredditRepository.addSubreddit(createSubreddit(id = "Subreddit1")).blockingAwait()
        subredditRepository.addSubreddit(createSubreddit(id = "Subreddit2")).blockingAwait()

        val feedRepository = DbFeedRepository(db.feedDao())
        val feeds = listOf("Feed1", "Feed2")
        feeds.forEach {
            feedRepository.addFeed(it).blockingAwait()
        }

        val postRepository = DbPostRepository(db, db.postDao(), db.postFeedDao())
        for (i in 0..50) {
            feeds.forEach { feed ->
                val postId = "${feed}Post${i + 1}"

                postRepository.addPosts(
                    listOf(createPost(id = postId, subredditId = "Subreddit1")), feed, SortType.Hot
                )
                    .blockingAwait()
            }
        }

        fetchFeedPosts = FetchFeedPosts(
            postRepository,
            subredditRepository,
            FakePostWebService(),
            FakeSubredditWebService(),
            DbAfterKeyRepository(db.feedAfterKeyDao()),
            feedRepository
        )

        val context = ApplicationProvider.getApplicationContext<Application>()
        presenter = FrontPagePresenter(
            view,
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
    fun `display posts by feed`() {
        //Act
        presenter.start("Feed2")

        //Assert
        assertThat(view.posts).extracting<String> { it.id }
            .allSatisfy { id -> id.contains("Feed2") }
    }

    @Test
    fun `format post`() {
        //Act
        presenter.start("Feed1")

        //Assert
        assertThat(view.posts).first().isEqualToIgnoringGivenFields(
            createFeedPostDto(),
            FeedPostDto::id.name,
            FeedPostDto::subredditId.name
        )
    }

    @Test
    fun `when end is not reached should display loading`() {
        //Act
        presenter.start("Feed1")

        //Assert
        assertThat(view.isLoading).isEqualTo(true)
    }

    /*@Test
    fun `when end is reached should hide loading`() {
        //Arrange
        db.clearAllTables()

        //Act
        presenter.start("Feed1")

        //Assert
        assertThat(view.isLoading).isEqualTo(false)
    }*/

    @Test
    fun `when near the end should load more`() {
        //Act
        presenter.start("Feed1")
        presenter.onPostBound(PAGE_SIZE * 3 / 4 + 1)

        //Assert
        assertThat(view.posts)
            .first()
            .extracting { it.id }
            .isEqualTo("Feed1Post${PAGINATION_STEP + 1}")
    }

    @Test
    fun `when near the top should load more`() {
        //Act
        presenter.start("Feed1")
        presenter.onPostBound(PAGE_SIZE)
        presenter.onPostBound(0)

        //Assert
        assertThat(view.posts)
            .first()
            .extracting { it.id }
            .isEqualTo("Feed1Post1")
    }

    /*@Test
    fun `display error when fetching posts fails`() {
        //Arrange

        //Act
        presenter.start("Feed")

        //Assert
    }*/

    class FakeFrontPageView : FrontPageView {
        var posts: List<FeedPostDto> = listOf()
        var isLoading = false

        override fun render(viewState: FrontPageViewState) {
            when (viewState) {
                FrontPageViewState.Loading -> ""
                is FrontPageViewState.Failure -> ""
                is FrontPageViewState.Success -> {
                    posts = viewState.posts
                    isLoading = viewState.isLoading
                }
            }
        }
    }
}