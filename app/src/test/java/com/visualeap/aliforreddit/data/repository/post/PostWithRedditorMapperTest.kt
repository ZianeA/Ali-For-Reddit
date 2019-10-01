package com.visualeap.aliforreddit.data.repository.post

import com.visualeap.aliforreddit.data.repository.redditor.RedditorEntity
import com.visualeap.aliforreddit.data.repository.Mapper
import com.visualeap.aliforreddit.data.repository.subreddit.SubredditEntity
import com.visualeap.aliforreddit.domain.model.Redditor
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
internal class PostWithRedditorMapperTest {
    private val redditorEntityMapper: Mapper<RedditorEntity, Redditor> = mockk()
    private val subredditEntityMapper: Mapper<SubredditEntity, Subreddit> = mockk()
    private val postWithRedditorMapper =
        PostWithRedditorMapper(redditorEntityMapper, subredditEntityMapper)

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
    }

    @Test
    fun `map PostWithRedditor to Post`() {
        //Arrange
        val postWithRedditor = createPostWithRedditor()
        val post = createPost()
        every { redditorEntityMapper.map(postWithRedditor.redditorEntity) } returns post.author
        every { subredditEntityMapper.map(postWithRedditor.subredditEntity) } returns post.subreddit

        //Act
        val mappedPost = postWithRedditorMapper.map(postWithRedditor)

        //Assert
        assertThat(mappedPost).isEqualTo(post)
    }

    @Test
    fun `map Post to PostWithRedditor`() {
        //Arrange
        val post = createPost()
        val postWithRedditor = createPostWithRedditor()
        every { redditorEntityMapper.mapReverse(post.author) } returns postWithRedditor.redditorEntity
        every { subredditEntityMapper.mapReverse(post.subreddit) } returns postWithRedditor.subredditEntity

        //Act
        val mappedPostWithRedditor = postWithRedditorMapper.mapReverse(post)

        //Assert
        assertThat(mappedPostWithRedditor).isEqualTo(postWithRedditor)
    }
}