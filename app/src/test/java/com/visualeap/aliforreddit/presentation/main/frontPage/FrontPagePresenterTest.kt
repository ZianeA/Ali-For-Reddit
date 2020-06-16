package com.visualeap.aliforreddit.presentation.main.frontPage

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.visualeap.aliforreddit.SyncSchedulerProvider
import com.visualeap.aliforreddit.data.database.RedditDatabase
import com.visualeap.aliforreddit.data.repository.afterkey.DbAfterKeyRepository
import com.visualeap.aliforreddit.data.repository.feed.DbFeedRepository
import com.visualeap.aliforreddit.data.repository.post.DbPostRepository
import com.visualeap.aliforreddit.data.repository.subreddit.DbSubredditRepository
import com.visualeap.aliforreddit.domain.model.feed.SortType
import com.visualeap.aliforreddit.domain.usecase.FetchFeedPosts
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

        presenter = FrontPagePresenter(view, fetchFeedPosts, SyncSchedulerProvider())
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
    fun `when near the end should load more`() {
        //Act
        presenter.start("Feed1")
        presenter.onScroll(Int.MAX_VALUE, PAGE_SIZE)

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
        presenter.onScroll(Int.MAX_VALUE, PAGE_SIZE)
        presenter.onScroll(0, Int.MAX_VALUE)

        //Assert
        assertThat(view.posts)
            .first()
            .extracting { it.id }
            .isEqualTo("Feed1Post1")
    }

    /*@Test
    fun `display error when fetching posts fails`() {

    }*/

    class FakeFrontPageView : FrontPageView {
        var posts: List<FeedPostDto> = listOf()

        override fun render(viewState: FrontPageViewState) {
            when (viewState) {
                FrontPageViewState.Loading -> ""
                FrontPageViewState.Failure -> ""
                is FrontPageViewState.Success -> posts = viewState.posts
            }
        }
    }
}