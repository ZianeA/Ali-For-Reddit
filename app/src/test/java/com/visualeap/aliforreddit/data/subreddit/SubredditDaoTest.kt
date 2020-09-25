package com.visualeap.aliforreddit.data.subreddit

import com.visualeap.aliforreddit.data.database.RedditDatabase
import org.assertj.core.api.Assertions.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import util.domain.createDatabase
import util.domain.createSubreddit

@RunWith(RobolectricTestRunner::class)
internal class SubredditDaoTest {
    private lateinit var db: RedditDatabase
    private lateinit var subredditDao: SubredditDao

    @Before
    fun setUp() {
        db = createDatabase()
        subredditDao = db.subredditDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun `add subreddit to database`() {
        //Arrange
        val subreddit = createSubreddit()

        //Act
        subredditDao.add(subreddit)
            .test()
            .assertResult()

        //Assert
        val actual = subredditDao.getByIds(listOf(subreddit.id)).blockingFirst()
        assertThat(actual).containsExactly(subreddit)
    }

    @Test
    fun `add many subreddits to database`() {
        //Arrange
        val subreddits =
            listOf(createSubreddit(id = "Subreddit1"), createSubreddit(id = "Subreddit2"))

        //Act
        subredditDao.addAll(subreddits)
            .test()
            .assertResult()

        //Assert
        val actual = subredditDao.getByIds(listOf("Subreddit1", "Subreddit2"))
            .blockingFirst()
        assertThat(actual).containsExactlyElementsOf(subreddits)
    }

    @Test
    fun `return subreddits by ids`() {
        //Arrange
        val subreddits =
            listOf(
                createSubreddit(id = "Subreddit1"),
                createSubreddit(id = "Subreddit2"),
                createSubreddit(id = "Subreddit3")
            )

        //Act
        subredditDao.addAll(subreddits)
            .test()
            .assertResult()

        //Assert
        val actual = subredditDao.getByIds(listOf("Subreddit1", "Subreddit3"))
            .blockingFirst()
        assertThat(actual).containsExactly(subreddits[0], subreddits[2])
    }
}