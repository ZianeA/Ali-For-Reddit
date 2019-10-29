package com.visualeap.aliforreddit.data.repository.comment

import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
internal class CommentDataRepositoryTest {
    /*private val commentDao: CommentDao = mockk()
    private val redditService: RedditService = mockk()
    private val repository = CommentDataRepository(commentDao, redditService)

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
    }*/

    //DataSource and DataSource.Factory makes unit testing very hard.
    /*@Test
    fun `return post's comments`() {
        //Arrange
        val post = createPost()
        every { commentDao.getByPostId(post.id) } returns mockDataSourceFactory()
        every { redditService.getCommentsByPost(post.subreddit.name, post.id) } returns Single.just(
            createCommentResponse()
        )

        //Act and assert
        val expectedPagedList = mockPagedList(listOf(createComment()))
        repository.getCommentsByPostId(post.id, {}, {})
            .test()
            .assertResult(expectedPagedList)
    }*/

    /*private fun <KEY, VALUE> mockDataSourceFactory(): DataSource.Factory<KEY, VALUE> {
        val factory: DataSource.Factory<KEY, VALUE> = mockk()
        every { factory.create() } returns
        return factory
    }*/
}