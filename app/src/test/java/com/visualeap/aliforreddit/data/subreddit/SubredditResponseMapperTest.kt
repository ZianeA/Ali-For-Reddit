package com.visualeap.aliforreddit.data.subreddit

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import util.domain.createSubreddit
import util.domain.createSubredditResponse

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SubredditResponseMapperTest {
    @Test
    fun `map SubredditResponse to Subreddit`() {
        //Act
        val mappedSubredditList = SubredditResponseMapper.map(createSubredditResponse())

        //Assert
        assertThat(mappedSubredditList).isEqualTo(listOf(createSubreddit()))
    }
}