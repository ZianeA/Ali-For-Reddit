package com.visualeap.aliforreddit.data.repository.post

import com.visualeap.aliforreddit.domain.util.Mapper
import com.visualeap.aliforreddit.data.repository.subreddit.SubredditResponse
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
import util.domain.createPost
import util.domain.createPostWithSubredditResponse
import util.domain.createSubreddit

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
internal class PostWithSubredditResponseMapperTest {
    private val subredditMapper: Mapper<SubredditResponse, List<Subreddit>> = mockk()
    private val postMapper = PostWithSubredditResponseMapper(subredditMapper)

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
    }

    @Test
    internal fun `map PostWithSubredditResponse to Post`() {
        //Arrange
        val postWithSubredditResponse = createPostWithSubredditResponse()
        every { subredditMapper.map(postWithSubredditResponse.subredditResponse) } returns listOf(
            createSubreddit()
        )

        //Act
        val mappedPostList = postMapper.map(postWithSubredditResponse)

        //Assert
        assertThat(mappedPostList).first().isEqualTo(createPost())
    }

    //Not needed
    /*@Test
    internal fun `map Post to PostWithSubredditResponse`() {
        //Arrange
        val postList = listOf(createPost())
        every { subredditMapper.mapReverse(postList.map { it.subreddit }) } returns createSubredditResponse()

        //Act
        val postWithSubredditResponse = postMapper.mapReverse(postList)

        //Assert
        assertThat(postWithSubredditResponse).isEqualTo(createPostWithSubredditResponse())
    }*/
}