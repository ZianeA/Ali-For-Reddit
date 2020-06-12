package com.visualeap.aliforreddit.presentation.main.frontPage

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.visualeap.aliforreddit.SyncSchedulerProvider
import com.visualeap.aliforreddit.data.repository.afterkey.DbAfterKeyRepository
import com.visualeap.aliforreddit.data.repository.feed.DbFeedRepository
import com.visualeap.aliforreddit.data.repository.post.DbPostRepository
import com.visualeap.aliforreddit.data.repository.subreddit.DbSubredditRepository
import com.visualeap.aliforreddit.domain.repository.PostRepository
import com.visualeap.aliforreddit.domain.usecase.FetchFeedPosts
import com.visualeap.aliforreddit.util.fake.FakePostWebService
import com.visualeap.aliforreddit.util.fake.FakeSubredditWebService
import io.mockk.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import util.domain.createDatabase

@RunWith(RobolectricTestRunner::class)
class FrontPagePresenterTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val view: FrontPageView = mockk()
    private lateinit var fetchFeedPosts: FetchFeedPosts
    private lateinit var presenter: FrontPagePresenter

    @Before
    internal fun setUp() {
        val db = createDatabase()
        fetchFeedPosts = FetchFeedPosts(
            DbPostRepository(db, db.postDao(), db.postFeedDao()),
            DbSubredditRepository(db.subredditDao()),
            FakePostWebService(),
            FakeSubredditWebService(),
            DbAfterKeyRepository(db.feedAfterKeyDao()),
            DbFeedRepository(db.feedDao())
        )

        presenter = FrontPagePresenter(view, fetchFeedPosts, SyncSchedulerProvider())
    }


}