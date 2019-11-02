package com.visualeap.aliforreddit.data.repository.post

import com.visualeap.aliforreddit.domain.util.Mapper
import com.visualeap.aliforreddit.data.repository.subreddit.SubredditEntity
import com.visualeap.aliforreddit.domain.model.Subreddit
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import util.domain.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
internal class PostWithSubredditMapperTest {
    private val subredditEntityMapper: Mapper<SubredditEntity, Subreddit> = mockk()
    private val postMapper = PostWithSubredditEntityMapper(subredditEntityMapper)

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
    }

    @Test
    fun `map PostWithSubredditEntity to Post`() {
        //Arrange
        val postWithSubreddit = createPostWithSubreddit()
        val post = createPost()
        every { subredditEntityMapper.map(postWithSubreddit.subredditEntity) } returns post.subreddit

        //Act
        val mappedPost = postMapper.map(postWithSubreddit)

        //Assert
        assertThat(mappedPost).isEqualTo(post)
    }

    @Test
    fun `map Post to PostWithSubredditEntity`() {
        //Arrange
        val post = createPost()
        val postWithSubreddit = createPostWithSubreddit()
        every { subredditEntityMapper.mapReverse(post.subreddit) } returns postWithSubreddit.subredditEntity

        //Act
        val mappedPostWithSubreddit = postMapper.mapReverse(post)

        //Assert
        assertThat(mappedPostWithSubreddit).isEqualTo(postWithSubreddit)
    }
}