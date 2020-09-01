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
import org.assertj.core.api.InstanceOfAssertFactories
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
        presenter.passEvents(PostDetailEvent.ScreenLoadEvent)

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
        presenter.passEvents(PostDetailEvent.ScreenLoadEvent)

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
        presenter.passEvents(PostDetailEvent.ScreenLoadEvent)

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
        presenter.passEvents(PostDetailEvent.ScreenLoadEvent)

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
        presenter.passEvents(PostDetailEvent.ScreenLoadEvent)

        // Assert
        testObserver.assertLastValue {
            assertThat(it.comments).containsExactly(createCommentDto())
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
        presenter.passEvents(PostDetailEvent.ScreenLoadEvent)

        //Assert
        testObserver.assertLastValue {
            assertThat(it.commentsError).isNotBlank()
            assertThat(it.commentsLoading).isFalse()
        }
    }

    /*

    *//*@Nested
    inner class Start {
        @Test
        fun `display post`() {
            //Arrange
            val post = createPostView()
            every { view.showPost(any()) } just runs
            every {
                commentRepository.getCommentsByPost(
                    any(),
                    any(),
                    any(),
                    any()
                )
            } returns Single.just(
                emptyList()
            )
            every { view.showComments(any()) } just runs

            //Act
            presenter.start(post)

            //Assert
            verify { view.showPost(post) }
        }

        @Test
        fun `pass selected post's comments to view`() {
            //Arrange
            val post = createPostView()
            val comments = listOf(createComment())
            val commentViews = listOf(createCommentView())
            every {
                commentRepository.getCommentsByPost(post.subreddit.name, post.id, any(), any())
            } returns Single.just(comments)
            every { commentViewMapper.mapReverse(comments) } returns commentViews
            every { view.showPost(any()) } just runs
            every { view.showComments(any()) } just runs

            //Act
            presenter.start(post)

            //Assert
            verify { view.showComments(commentViews) }
        }
    }*//*

    @Nested
    inner class OnCommentLongClicked {
        @Test
        fun `collapse comments when expanded`() {
            //Arrange
            val commentView = createCommentDto()
            every { view.showComments(any()) } just runs

            //Act
            presenter.onCommentLongClicked(commentView, listOf(commentView))

            //Assert
            verify { view.showComments(listOf(commentView.copy(isCollapsed = true))) }
        }

        @Test
        fun `handle collapsing deeply nested comments`() {
            //Arrange
            val clickedComment = createCommentDto(
                id = "16",
                parentId = "15",
                replies = null
            )
            val allComments = listOf(
                createCommentDto(
                    id = "1", parentId = null, replies = listOf(
                        createCommentDto(
                            id = "2", parentId = "1", replies = listOf(
                                createCommentDto(
                                    id = "5", parentId = "2", replies = listOf(
                                        createCommentDto(
                                            id = "7", parentId = "5", replies = listOf(
                                                createCommentDto(
                                                    id = "8",
                                                    parentId = "7",
                                                    replies = null
                                                )
                                            )
                                        )
                                    )
                                ),
                                createCommentDto(
                                    id = "6", parentId = "2", replies = listOf(
                                        createCommentDto(
                                            id = "12", parentId = "6", replies = listOf(
                                                createCommentDto(
                                                    id = "15", parentId = "12", replies = listOf(
                                                        clickedComment
                                                    )
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
            every { view.showComments(any()) } just runs

            //Act
            presenter.onCommentLongClicked(clickedComment, allComments)

            //Assert
            val expectedComments = listOf(
                createCommentDto(
                    id = "1", parentId = null, replies = listOf(
                        createCommentDto(
                            id = "2", parentId = "1", replies = listOf(
                                createCommentDto(
                                    id = "5", parentId = "2", replies = listOf(
                                        createCommentDto(
                                            id = "7", parentId = "5", replies = listOf(
                                                createCommentDto(
                                                    id = "8",
                                                    parentId = "7",
                                                    replies = null
                                                )
                                            )
                                        )
                                    )
                                ),
                                createCommentDto(
                                    id = "6", parentId = "2", replies = listOf(
                                        createCommentDto(
                                            id = "12", parentId = "6", replies = listOf(
                                                createCommentDto(
                                                    id = "15", parentId = "12", replies = listOf(
                                                        clickedComment.copy(isCollapsed = true)
                                                    )
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
            verify { view.showComments(expectedComments) }
        }

        @Test
        fun `handle collapsing deeply nested comments 2`() {
            //Arrange
            val clickedComment = createCommentDto(
                id = "17",
                parentId = "14",
                replies = null
            )
            val allComments = listOf(
                createCommentDto(
                    id = "1", parentId = null, replies = listOf(
                        createCommentDto(
                            id = "2", parentId = "1", replies = listOf(
                                createCommentDto(
                                    id = "5", parentId = "2", replies = listOf(
                                        createCommentDto(
                                            id = "7", parentId = "5", replies = listOf(
                                                createCommentDto(
                                                    id = "8",
                                                    parentId = "7",
                                                    replies = null
                                                )
                                            )
                                        )
                                    )
                                ),
                                createCommentDto(
                                    id = "6", parentId = "2", replies = listOf(
                                        createCommentDto(
                                            id = "12", parentId = "6", replies = listOf(
                                                createCommentDto(
                                                    id = "15", parentId = "12", replies = listOf(
                                                        createCommentDto(
                                                            id = "16",
                                                            parentId = "15",
                                                            replies = null
                                                        )
                                                    )
                                                )
                                            )
                                        ),
                                        createCommentDto(
                                            id = "13",
                                            parentId = "6",
                                            replies = null
                                        ),
                                        createCommentDto(
                                            id = "14", parentId = "6", replies = listOf(
                                                clickedComment,
                                                createCommentDto(
                                                    id = "18",
                                                    parentId = "14",
                                                    replies = null
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
            every { view.showComments(any()) } just runs

            //Act
            presenter.onCommentLongClicked(clickedComment, allComments)

            //Assert
            val expectedComments = listOf(
                createCommentDto(
                    id = "1", parentId = null, replies = listOf(
                        createCommentDto(
                            id = "2", parentId = "1", replies = listOf(
                                createCommentDto(
                                    id = "5", parentId = "2", replies = listOf(
                                        createCommentDto(
                                            id = "7", parentId = "5", replies = listOf(
                                                createCommentDto(
                                                    id = "8",
                                                    parentId = "7",
                                                    replies = null
                                                )
                                            )
                                        )
                                    )
                                ),
                                createCommentDto(
                                    id = "6", parentId = "2", replies = listOf(
                                        createCommentDto(
                                            id = "12", parentId = "6", replies = listOf(
                                                createCommentDto(
                                                    id = "15", parentId = "12", replies = listOf(
                                                        createCommentDto(
                                                            id = "16",
                                                            parentId = "15",
                                                            replies = null
                                                        )
                                                    )
                                                )
                                            )
                                        ),
                                        createCommentDto(
                                            id = "13",
                                            parentId = "6",
                                            replies = null
                                        ),
                                        createCommentDto(
                                            id = "14", parentId = "6", replies = listOf(
                                                clickedComment.copy(isCollapsed = true),
                                                createCommentDto(
                                                    id = "18",
                                                    parentId = "14",
                                                    replies = null
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
            verify { view.showComments(expectedComments) }
        }

        @Test
        fun `handle collapsing other root comments`() {
            //Arrange
            val clickedComment = createCommentDto(
                id = "10", parentId = null, replies = listOf(
                    createCommentDto(id = "11", parentId = "10", replies = null)
                )
            )
            val allComments = listOf(
                createCommentDto(
                    id = "1", parentId = null, replies = listOf(
                        createCommentDto(
                            id = "2", parentId = "1", replies = listOf(
                                createCommentDto(
                                    id = "5", parentId = "2", replies = listOf(
                                        createCommentDto(
                                            id = "7", parentId = "5", replies = listOf(
                                                createCommentDto(
                                                    id = "8",
                                                    parentId = "7",
                                                    replies = null
                                                )
                                            )
                                        )
                                    )
                                ),
                                createCommentDto(
                                    id = "6", parentId = "2", replies = listOf(
                                        createCommentDto(
                                            id = "12", parentId = "6", replies = listOf(
                                                createCommentDto(
                                                    id = "15", parentId = "12", replies = listOf(
                                                        createCommentDto(
                                                            id = "16",
                                                            parentId = "15",
                                                            replies = null
                                                        )
                                                    )
                                                )
                                            )
                                        ),
                                        createCommentDto(
                                            id = "13",
                                            parentId = "6",
                                            replies = null
                                        ),
                                        createCommentDto(
                                            id = "14", parentId = "6", replies = listOf(
                                                createCommentDto(
                                                    id = "17",
                                                    parentId = "14",
                                                    replies = null
                                                ),
                                                createCommentDto(
                                                    id = "18",
                                                    parentId = "14",
                                                    replies = null
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                        ),
                        createCommentDto(id = "3", parentId = "1", replies = null),
                        createCommentDto(id = "4", parentId = "1", replies = null)
                    )
                ),
                createCommentDto(id = "9", parentId = null, replies = null),
                clickedComment
            )
            every { view.showComments(any()) } just runs

            //Act
            presenter.onCommentLongClicked(clickedComment, allComments)

            //Assert
            val expectedComments = listOf(
                createCommentDto(
                    id = "1", parentId = null, replies = listOf(
                        createCommentDto(
                            id = "2", parentId = "1", replies = listOf(
                                createCommentDto(
                                    id = "5", parentId = "2", replies = listOf(
                                        createCommentDto(
                                            id = "7", parentId = "5", replies = listOf(
                                                createCommentDto(
                                                    id = "8",
                                                    parentId = "7",
                                                    replies = null
                                                )
                                            )
                                        )
                                    )
                                ),
                                createCommentDto(
                                    id = "6", parentId = "2", replies = listOf(
                                        createCommentDto(
                                            id = "12", parentId = "6", replies = listOf(
                                                createCommentDto(
                                                    id = "15", parentId = "12", replies = listOf(
                                                        createCommentDto(
                                                            id = "16",
                                                            parentId = "15",
                                                            replies = null
                                                        )
                                                    )
                                                )
                                            )
                                        ),
                                        createCommentDto(
                                            id = "13",
                                            parentId = "6",
                                            replies = null
                                        ),
                                        createCommentDto(
                                            id = "14", parentId = "6", replies = listOf(
                                                createCommentDto(
                                                    id = "17",
                                                    parentId = "14",
                                                    replies = null
                                                ),
                                                createCommentDto(
                                                    id = "18",
                                                    parentId = "14",
                                                    replies = null
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                        ),
                        createCommentDto(id = "3", parentId = "1", replies = null),
                        createCommentDto(id = "4", parentId = "1", replies = null)
                    )
                ),
                createCommentDto(id = "9", parentId = null, replies = null),
                clickedComment.copy(isCollapsed = true)
            )
            verify { view.showComments(expectedComments) }
        }

        @Test
        fun `handle collapsing nested comment of other root comments`() {
            //Arrange
            val clickedComment = createCommentDto(id = "11", parentId = "10", replies = null)
            val allComments = listOf(
                createCommentDto(
                    id = "1", parentId = null, replies = listOf(
                        createCommentDto(
                            id = "2", parentId = "1", replies = listOf(
                                createCommentDto(
                                    id = "5", parentId = "2", replies = listOf(
                                        createCommentDto(
                                            id = "7", parentId = "5", replies = listOf(
                                                createCommentDto(
                                                    id = "8",
                                                    parentId = "7",
                                                    replies = null
                                                )
                                            )
                                        )
                                    )
                                ),
                                createCommentDto(
                                    id = "6", parentId = "2", replies = listOf(
                                        createCommentDto(
                                            id = "12", parentId = "6", replies = listOf(
                                                createCommentDto(
                                                    id = "15", parentId = "12", replies = listOf(
                                                        createCommentDto(
                                                            id = "16",
                                                            parentId = "15",
                                                            replies = null
                                                        )
                                                    )
                                                )
                                            )
                                        ),
                                        createCommentDto(
                                            id = "13",
                                            parentId = "6",
                                            replies = null
                                        ),
                                        createCommentDto(
                                            id = "14", parentId = "6", replies = listOf(
                                                createCommentDto(
                                                    id = "17",
                                                    parentId = "14",
                                                    replies = null
                                                ),
                                                createCommentDto(
                                                    id = "18",
                                                    parentId = "14",
                                                    replies = null
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                        ),
                        createCommentDto(id = "3", parentId = "1", replies = null),
                        createCommentDto(id = "4", parentId = "1", replies = null)
                    )
                ),
                createCommentDto(id = "9", parentId = null, replies = null),
                createCommentDto(
                    id = "10", parentId = null, replies = listOf(clickedComment)
                )
            )
            every { view.showComments(any()) } just runs

            //Act
            presenter.onCommentLongClicked(clickedComment, allComments)

            //Assert
            val expectedComment = listOf(
                createCommentDto(
                    id = "1", parentId = null, replies = listOf(
                        createCommentDto(
                            id = "2", parentId = "1", replies = listOf(
                                createCommentDto(
                                    id = "5", parentId = "2", replies = listOf(
                                        createCommentDto(
                                            id = "7", parentId = "5", replies = listOf(
                                                createCommentDto(
                                                    id = "8",
                                                    parentId = "7",
                                                    replies = null
                                                )
                                            )
                                        )
                                    )
                                ),
                                createCommentDto(
                                    id = "6", parentId = "2", replies = listOf(
                                        createCommentDto(
                                            id = "12", parentId = "6", replies = listOf(
                                                createCommentDto(
                                                    id = "15", parentId = "12", replies = listOf(
                                                        createCommentDto(
                                                            id = "16",
                                                            parentId = "15",
                                                            replies = null
                                                        )
                                                    )
                                                )
                                            )
                                        ),
                                        createCommentDto(
                                            id = "13",
                                            parentId = "6",
                                            replies = null
                                        ),
                                        createCommentDto(
                                            id = "14", parentId = "6", replies = listOf(
                                                createCommentDto(
                                                    id = "17",
                                                    parentId = "14",
                                                    replies = null
                                                ),
                                                createCommentDto(
                                                    id = "18",
                                                    parentId = "14",
                                                    replies = null
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                        ),
                        createCommentDto(id = "3", parentId = "1", replies = null),
                        createCommentDto(id = "4", parentId = "1", replies = null)
                    )
                ),
                createCommentDto(id = "9", parentId = null, replies = null),
                createCommentDto(
                    id = "10",
                    parentId = null,
                    replies = listOf(clickedComment.copy(isCollapsed = true))
                )
            )
            verify { view.showComments(expectedComment) }
        }

        @Test
        fun `expand comments when collapsed`() {
            //Arrange
            val commentView = createCommentDto(isCollapsed = true)
            every { view.showComments(any()) } just runs

            //Act
            presenter.onCommentLongClicked(commentView, listOf(commentView))

            //Assert
            verify { view.showComments(listOf(commentView.copy(isCollapsed = false))) }
        }

        @Test
        fun `handle expanding deeply nested comments`() {
            //Arrange
            val clickedComment = createCommentDto(
                id = "16",
                parentId = "15",
                replies = null,
                isCollapsed = true
            )
            val allComments = listOf(
                createCommentDto(
                    id = "1", parentId = null, replies = listOf(
                        createCommentDto(
                            id = "2", parentId = "1", replies = listOf(
                                createCommentDto(
                                    id = "5", parentId = "2", replies = listOf(
                                        createCommentDto(
                                            id = "7", parentId = "5", replies = listOf(
                                                createCommentDto(
                                                    id = "8",
                                                    parentId = "7",
                                                    replies = null
                                                )
                                            )
                                        )
                                    )
                                ),
                                createCommentDto(
                                    id = "6", parentId = "2", replies = listOf(
                                        createCommentDto(
                                            id = "12", parentId = "6", replies = listOf(
                                                createCommentDto(
                                                    id = "15", parentId = "12", replies = listOf(
                                                        clickedComment
                                                    )
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
            every { view.showComments(any()) } just runs

            //Act
            presenter.onCommentLongClicked(clickedComment, allComments)

            //Assert
            val expectedComments = listOf(
                createCommentDto(
                    id = "1", parentId = null, replies = listOf(
                        createCommentDto(
                            id = "2", parentId = "1", replies = listOf(
                                createCommentDto(
                                    id = "5", parentId = "2", replies = listOf(
                                        createCommentDto(
                                            id = "7", parentId = "5", replies = listOf(
                                                createCommentDto(
                                                    id = "8",
                                                    parentId = "7",
                                                    replies = null
                                                )
                                            )
                                        )
                                    )
                                ),
                                createCommentDto(
                                    id = "6", parentId = "2", replies = listOf(
                                        createCommentDto(
                                            id = "12", parentId = "6", replies = listOf(
                                                createCommentDto(
                                                    id = "15", parentId = "12", replies = listOf(
                                                        clickedComment.copy(isCollapsed = false)
                                                    )
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
            verify { view.showComments(expectedComments) }
        }
    }*/
}