package com.visualeap.aliforreddit.presentation.postDetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.visualeap.aliforreddit.domain.comment.GetCommentsByPost
import com.visualeap.aliforreddit.domain.post.GetPostById
import com.visualeap.aliforreddit.domain.util.Lce
import com.visualeap.aliforreddit.presentation.common.util.ResourceProvider
import com.visualeap.aliforreddit.util.TrampolineSchedulerProvider
import io.mockk.*
import io.reactivex.Observable
import org.assertj.core.api.Assertions.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import util.domain.*
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
internal class PostDetailPresenterTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val launcher: PostDetailLauncher = mockk(relaxed = true)
    private val getPostById: GetPostById = mockk(relaxed = true)
    private val getCommentsByPost: GetCommentsByPost = mockk(relaxed = true)
    private val presenter = PostDetailPresenter(
        launcher,
        POST_ID,
        SUBREDDIT_NAME,
        getPostById,
        getCommentsByPost,
        ResourceProvider(ApplicationProvider.getApplicationContext()),
        TrampolineSchedulerProvider()
    )

    @Before
    internal fun setup() {
        every { getPostById.execute(any()) } returns Observable.never()
        every { getCommentsByPost.execute(any(), any()) } returns Observable.never()
    }

    @After
    internal fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `display progress bar when post is loading`() {
        //Arrange
        every { getPostById.execute(any()) } returns Observable.just(Lce.Loading())

        //Act
        val testObserver = presenter.viewState.test()
        presenter.passEvent(PostDetailEvent.ScreenLoadEvent)

        //Assert
        testObserver.assertLastValue { assertThat(it.postLoading).isTrue() }
    }

    @Test
    fun `display post detail when loaded`() {
        //Arrange
        every { getPostById.execute(any()) }
            .returns(Observable.just(Lce.Content(createSubreddit() to createPost())))

        //Act
        val testObserver = presenter.viewState.test()
        presenter.passEvent(PostDetailEvent.ScreenLoadEvent)

        //Assert
        testObserver.assertLastValue {
            assertThat(it.post).isEqualTo(createPostDto())
            assertThat(it.postLoading).isFalse()
        }
    }

    @Test
    fun `display error when loading post fails`() {
        //Arrange
        every { getPostById.execute(any()) } returns Observable.just(Lce.Error(IOException()))

        //Act
        val testObserver = presenter.viewState.test()
        presenter.passEvent(PostDetailEvent.ScreenLoadEvent)

        //Assert
        testObserver.assertLastValue {
            assertThat(it.postError).isNotBlank()
            assertThat(it.postLoading).isFalse()
        }
    }

    @Test
    fun `display progress bar when comments are loading`() {
        //Arrange
        every { getCommentsByPost.execute(any(), any()) } returns Observable.just(Lce.Loading())

        //Act
        val testObserver = presenter.viewState.test()
        presenter.passEvent(PostDetailEvent.ScreenLoadEvent)

        //Assert
        testObserver.assertLastValue { assertThat(it.commentsLoading).isTrue() }
    }

    @Test
    fun `display comments when loaded`() {
        //Arrange
        every { getCommentsByPost.execute(any(), any()) }
            .returns(Observable.just(Lce.Content(listOf(createComment()))))

        //Act
        val testObserver = presenter.viewState.test()
        presenter.passEvent(PostDetailEvent.ScreenLoadEvent)

        // Assert
        testObserver.assertLastValue {
            assertThat(it.comments).containsExactly(createComment())
            assertThat(it.commentsLoading).isFalse()
        }
    }

    @Test
    fun `display error when loading comments fails`() {
        //Arrange
        every { getCommentsByPost.execute(any(), any()) }
            .returns(Observable.just(Lce.Error(IOException())))

        //Act
        val testObserver = presenter.viewState.test()
        presenter.passEvent(PostDetailEvent.ScreenLoadEvent)

        //Assert
        testObserver.assertLastValue {
            assertThat(it.commentsError).isNotBlank()
            assertThat(it.commentsLoading).isFalse()
        }
    }
}