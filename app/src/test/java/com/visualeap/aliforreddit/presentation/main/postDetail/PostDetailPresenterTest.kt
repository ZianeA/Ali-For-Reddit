package com.visualeap.aliforreddit.presentation.main.postDetail

import com.visualeap.aliforreddit.SyncSchedulerProvider
import com.visualeap.aliforreddit.domain.repository.CommentRepository
import io.mockk.*
import io.mockk.junit5.MockKExtension
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import util.domain.createComment
import util.domain.createPost

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
internal class PostDetailPresenterTest {
    private val view: PostDetailView = mockk()
    private val commentRepository: CommentRepository = mockk(relaxed = true)
    private val presenter =
        PostDetailPresenter(view, commentRepository, SyncSchedulerProvider())

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
    }

    @Nested
    inner class Start {
        @Test
        fun `display post`() {
            //Arrange
            val post = createPost()
            every { view.showPost(any()) } just runs

            //Act
            presenter.start(post)

            //Assert
            verify { view.showPost(post) }
        }

        @Test
        fun `pass selected post's comments to view`() {
            //Arrange
            val post = createPost()
            val comments = listOf(createComment())
            every {
                commentRepository.getCommentsByPost(post.id, post.subreddit.name, any(), any())
            } returns Single.just(comments)
            every { view.showPost(any()) } just runs
            every { view.showComments(any()) } just runs

            //Act
            presenter.start(post)

            //Assert
            verify { view.showComments(comments) }
        }
    }
}