package com.visualeap.aliforreddit.presentation.main.frontPage

import com.visualeap.aliforreddit.SyncSchedulerProvider
import com.visualeap.aliforreddit.domain.model.Post
import com.visualeap.aliforreddit.domain.repository.PostRepository
import io.mockk.*
import io.mockk.junit5.MockKExtension
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import util.domain.createPost

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
class FrontPagePresenterTest {

    companion object {
        private val POSTS_LIST = listOf(createPost())
        private val POST_LIST_SINGLE = Single.just(POSTS_LIST)
    }

    private val view: FrontPageView = mockk(relaxed = true)

    private val repository: PostRepository = mockk()

    private val presenter = FrontPagePresenter(view, repository, SyncSchedulerProvider())

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
    }

    @Test
    fun passPostsToView() {
        //Arrange
        every { repository.getPosts() } returns POST_LIST_SINGLE

        //Act
        presenter.start()

        //Assert
        verify(atMost = 1) { view.displayPosts(POSTS_LIST) }
    }
}