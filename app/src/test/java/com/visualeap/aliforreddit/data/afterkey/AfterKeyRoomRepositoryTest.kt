package com.visualeap.aliforreddit.data.afterkey

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.visualeap.aliforreddit.data.database.RedditDatabase
import com.visualeap.aliforreddit.domain.post.AfterKey
import com.visualeap.aliforreddit.domain.feed.SortType
import com.visualeap.aliforreddit.domain.post.AfterKeyRepository
import org.assertj.core.api.Assertions.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import util.domain.*

@RunWith(RobolectricTestRunner::class)
internal class AfterKeyRoomRepositoryTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var db: RedditDatabase
    lateinit var afterKeyRepository: AfterKeyRepository

    @Before
    fun setUp() {
        db = createDatabase()

        // Add a record to the feed table
        db.feedDao().add(createFeedEntity()).blockingGet()

        afterKeyRepository = AfterKeyRoomRepository(db.feedAfterKeyDao())
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun `when there is no key, should return Empty`() {
        //Act and assert
        afterKeyRepository.getAfterKey(FEED_NAME, SortType.Best)
            .test()
            .assertValue(match { assertThat(it).isEqualTo(AfterKey.Empty) })
    }

    @Test
    fun `when key is blank should return End`() {
        //Arrange
        afterKeyRepository.setAfterKey(FEED_NAME, SortType.Best, AfterKey.End).blockingGet()

        //Act and assert
        afterKeyRepository.getAfterKey(FEED_NAME, SortType.Best)
            .test()
            .assertValue(match { assertThat(it).isEqualTo(AfterKey.End) })
    }

    @Test
    fun `when key is present should return Next`() {
        //Arrange
        afterKeyRepository.setAfterKey(FEED_NAME, SortType.Best, AfterKey.Next("FakeAfterKey"))
            .blockingGet()

        //Act and assert
        afterKeyRepository.getAfterKey(FEED_NAME, SortType.Best)
            .test()
            .assertValue(match { assertThat(it).isEqualTo(AfterKey.Next("FakeAfterKey")) })
    }

    @Test
    fun `when setting key to Empty should return error`() {
        //Act and assert
        afterKeyRepository.setAfterKey(FEED_NAME, SortType.Best, AfterKey.Empty)
            .test()
            .assertFailure(IllegalArgumentException::class.java)
    }

    @Test
    fun `should return key by feed and sort type`() {
        //Arrange
        afterKeyRepository.setAfterKey(FEED_NAME, SortType.Best, AfterKey.Next("FakeAfterKey"))
            .blockingGet()

        val newFeed = "MyFeed"
        db.feedDao().add(createFeedEntity(name = newFeed)).blockingGet()
        afterKeyRepository.setAfterKey(newFeed, SortType.Rising, AfterKey.Next("MyFeedAfterKey"))
            .blockingGet()

        //Act and assert
        afterKeyRepository.getAfterKey(newFeed, SortType.Rising)
            .test()
            .assertValue(match { assertThat(it).isEqualTo(AfterKey.Next("MyFeedAfterKey")) })
    }
}