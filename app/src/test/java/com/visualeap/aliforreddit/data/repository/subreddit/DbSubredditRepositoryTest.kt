package com.visualeap.aliforreddit.data.repository.subreddit

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.test.core.app.ApplicationProvider
import com.visualeap.aliforreddit.data.database.RedditDatabase
import com.visualeap.aliforreddit.data.repository.post.DbPostRepository
import com.visualeap.aliforreddit.domain.repository.SubredditRepository
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import util.domain.createDatabase
import util.domain.createSubreddit
import util.domain.createSubredditEntity

@RunWith(RobolectricTestRunner::class)
internal class DbSubredditRepositoryTest {
    private lateinit var db: RedditDatabase
    private lateinit var subredditRepository: SubredditRepository

    @Before
    fun setUp() {
        db = createDatabase()
        subredditRepository = DbSubredditRepository(db.subredditDao())
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
        subredditRepository.addSubreddit(subreddit)
            .test()
            .assertResult()

        //Assert
        val actual = subredditRepository.getSubredditsByIds(listOf(subreddit.id)).blockingFirst()
        assertThat(actual).containsExactly(subreddit)
    }

    @Test
    fun `add many subreddits to database`() {
        //Arrange
        val subreddits =
            listOf(createSubreddit(id = "Subreddit1"), createSubreddit(id = "Subreddit2"))

        //Act
        subredditRepository.addSubreddits(subreddits)
            .test()
            .assertResult()

        //Assert
        val actual = subredditRepository.getSubredditsByIds(listOf("Subreddit1", "Subreddit2"))
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
        subredditRepository.addSubreddits(subreddits)
            .test()
            .assertResult()

        //Assert
        val actual = subredditRepository.getSubredditsByIds(listOf("Subreddit1", "Subreddit3"))
            .blockingFirst()
        assertThat(actual).containsExactly(subreddits[0], subreddits[2])
    }
}