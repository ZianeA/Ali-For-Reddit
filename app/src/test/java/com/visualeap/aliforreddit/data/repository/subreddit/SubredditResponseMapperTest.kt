package com.visualeap.aliforreddit.data.repository.subreddit

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import util.domain.createSubreddit
import util.domain.createSubredditResponse

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SubredditResponseMapperTest {
    private val mapper = SubredditResponseMapper()

    @Test
    fun `map SubredditResponse to Subreddit`() {
        //Act
        val mappedSubredditList = mapper.map(createSubredditResponse())

        //Assert
        assertThat(mappedSubredditList).isEqualTo(listOf(createSubreddit()))
    }

    //Not needed
    /*@Test
    fun `map Subreddit to SubreddtiResponse`() {
        //Act
        val subredditResponse = mapper.mapReverse(listOf(createSubreddit()))

        //Assert
        assertThat(subredditResponse).isEqualTo(createSubredditResponse())
    }*/
}