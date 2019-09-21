package com.visualeap.aliforreddit.data.network.post

import com.visualeap.aliforreddit.data.network.RedditService
import com.visualeap.aliforreddit.domain.model.Post
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
internal class PostRsTest {
    private val redditService: RedditService = mockk()
    private val postRs = PostRs(redditService)

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
    }

    @Test
    fun `return post`() {
        //Arrange
        val subredditName = "AnySubredditName"
        val post = PostResponse.Data.PostHolder.Post(
            "AnyId",
            "AnyAuthorName",
            "AnyTitle",
            "AnyText",
            101,
            201
        )
        val postResponse = PostResponse(
            PostResponse.Data(
                "TestKey",
                listOf(
                    PostResponse.Data.PostHolder(post),
                    PostResponse.Data.PostHolder(post),
                    PostResponse.Data.PostHolder(post)
                )
            )
        )
        every { redditService.getPosts(subredditName) } returns Single.just(postResponse)

        //Act, Assert
        val expectedPost =
            postResponse.data.postHolders.first().post.run {
                Post(
                    id,
                    authorName,
                    title,
                    text,
                    score,
                    commentCount
                )
            }
        postRs.getPosts(subredditName)
            .test()
            .assertResult(listOf(expectedPost, expectedPost, expectedPost))
    }
}