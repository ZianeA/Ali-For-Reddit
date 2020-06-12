package com.visualeap.aliforreddit.data.repository.feed

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.visualeap.aliforreddit.data.database.RedditDatabase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import util.domain.createDatabase

@RunWith(RobolectricTestRunner::class)
internal class DbFeedRepositoryTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: RedditDatabase
    private lateinit var repository: DbFeedRepository

    @Before
    internal fun setUp() {
        db = createDatabase()
        repository = DbFeedRepository(db.feedDao())
    }

    @After
    fun tearDown() {
        db.close()
    }

    /*@Test
    fun `when feed already exists should ignore`() {
        //Arrange

        //Act
        repository.addFeed("Feed")
            .

        //Assert
    }*/
}