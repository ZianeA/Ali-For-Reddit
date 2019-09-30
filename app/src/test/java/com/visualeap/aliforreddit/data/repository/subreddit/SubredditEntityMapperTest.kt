package com.visualeap.aliforreddit.data.repository.subreddit

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import util.domain.createSubreddit
import util.domain.createSubredditEntity

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class SubredditEntityMapperTest {
    private val mapper = SubredditEntityMapper()

    @Test
    internal fun `map SubredditEntity to Subreddit`() {
        //Act
        val mappedSubreddit = mapper.map(createSubredditEntity())

        //Assert
        assertThat(mappedSubreddit).isEqualTo(createSubreddit())
    }

    @Test
    internal fun `map Subreddit to SubredditEntity`() {
        //Act
        val mappedSubredditEntity = mapper.mapReverse(createSubreddit())

        //Assert
        assertThat(mappedSubredditEntity).isEqualTo(createSubredditEntity())
    }
}