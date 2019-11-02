package com.visualeap.aliforreddit.presentation.mapper

import com.visualeap.aliforreddit.domain.model.Subreddit
import com.visualeap.aliforreddit.domain.util.Mapper
import com.visualeap.aliforreddit.presentation.model.SubredditView
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import util.domain.createPost
import util.domain.createPostView
import util.domain.createSubreddit
import util.domain.createSubredditView

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
internal class PostViewMapperTest{
    private val subredditViewMapper: Mapper<SubredditView, Subreddit> = mockk()
    private val postViewMapper = PostViewMapper(subredditViewMapper)

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
    }

    @Test
    fun `map Post to PostView`() {
        //Arrange
        every { subredditViewMapper.mapReverse(createSubreddit()) } returns createSubredditView()

        //Act
        val mappedPostView = postViewMapper.mapReverse(createPost())

        //Assert
        assertThat(mappedPostView).isEqualTo(createPostView())
    }
}