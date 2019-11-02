package com.visualeap.aliforreddit.presentation.mapper

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import util.domain.createSubreddit
import util.domain.createSubredditView

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class SubredditViewMapperTest {
    private val mapper = SubredditViewMapper()

    @Test
    fun `map Subreddit to SubredditView`() {
        //Act
        val mappedSubredditView = mapper.mapReverse(createSubreddit())

        //Assert
        assertThat(mappedSubredditView).isEqualTo(createSubredditView())
    }

    @Test
    fun `return keyColor when primaryColor is null`() {
        //Arrange
        val subreddit = createSubreddit(primaryColor = null)

        //Act
        val mappedSubredditView = mapper.mapReverse(subreddit)

        //Assert
        assertThat(mappedSubredditView).isEqualTo(createSubredditView(color = subreddit.keyColor!!))
    }

    @Test
    fun `return default color when both primaryColor & keyColor are null`() {
        //Arrange
        val subreddit = createSubreddit(primaryColor = null, keyColor = null)

        //Act
        val mappedSubredditView = mapper.mapReverse(subreddit)

        //Assert
        assertThat(mappedSubredditView).isEqualTo(createSubredditView(color = "#33a8ff"))
    }
}