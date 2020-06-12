package com.visualeap.aliforreddit.presentation.main.postDetail

import com.visualeap.aliforreddit.SyncSchedulerProvider
import com.visualeap.aliforreddit.domain.model.Comment
import com.visualeap.aliforreddit.domain.repository.CommentRepository
import com.visualeap.aliforreddit.domain.util.Mapper
import com.visualeap.aliforreddit.presentation.model.CommentView
import io.mockk.*
import io.mockk.junit5.MockKExtension
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import util.domain.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
internal class PostDetailPresenterTest {
    private val view: PostDetailView = mockk()
    private val commentRepository: CommentRepository = mockk()
    private val commentViewMapper: Mapper<List<CommentView>, List<Comment>> = mockk()
    private val presenter =
        PostDetailPresenter(view, commentRepository, SyncSchedulerProvider(), commentViewMapper)

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
    }

    /*@Nested
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
    }*/

    @Nested
    inner class OnCommentLongClicked {
        @Test
        fun `collapse comments when expanded`() {
            //Arrange
            val commentView = createCommentView()
            every { view.showComments(any()) } just runs

            //Act
            presenter.onCommentLongClicked(commentView, listOf(commentView))

            //Assert
            verify { view.showComments(listOf(commentView.copy(isCollapsed = true))) }
        }

        @Test
        fun `handle collapsing deeply nested comments`() {
            //Arrange
            val clickedComment = createCommentView(
                id = "16",
                parentId = "15",
                replies = null
            )
            val allComments = listOf(
                createCommentView(
                    id = "1", parentId = null, replies = listOf(
                        createCommentView(
                            id = "2", parentId = "1", replies = listOf(
                                createCommentView(
                                    id = "5", parentId = "2", replies = listOf(
                                        createCommentView(
                                            id = "7", parentId = "5", replies = listOf(
                                                createCommentView(
                                                    id = "8",
                                                    parentId = "7",
                                                    replies = null
                                                )
                                            )
                                        )
                                    )
                                ),
                                createCommentView(
                                    id = "6", parentId = "2", replies = listOf(
                                        createCommentView(
                                            id = "12", parentId = "6", replies = listOf(
                                                createCommentView(
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
                createCommentView(
                    id = "1", parentId = null, replies = listOf(
                        createCommentView(
                            id = "2", parentId = "1", replies = listOf(
                                createCommentView(
                                    id = "5", parentId = "2", replies = listOf(
                                        createCommentView(
                                            id = "7", parentId = "5", replies = listOf(
                                                createCommentView(
                                                    id = "8",
                                                    parentId = "7",
                                                    replies = null
                                                )
                                            )
                                        )
                                    )
                                ),
                                createCommentView(
                                    id = "6", parentId = "2", replies = listOf(
                                        createCommentView(
                                            id = "12", parentId = "6", replies = listOf(
                                                createCommentView(
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
            val clickedComment = createCommentView(
                id = "17",
                parentId = "14",
                replies = null
            )
            val allComments = listOf(
                createCommentView(
                    id = "1", parentId = null, replies = listOf(
                        createCommentView(
                            id = "2", parentId = "1", replies = listOf(
                                createCommentView(
                                    id = "5", parentId = "2", replies = listOf(
                                        createCommentView(
                                            id = "7", parentId = "5", replies = listOf(
                                                createCommentView(
                                                    id = "8",
                                                    parentId = "7",
                                                    replies = null
                                                )
                                            )
                                        )
                                    )
                                ),
                                createCommentView(
                                    id = "6", parentId = "2", replies = listOf(
                                        createCommentView(
                                            id = "12", parentId = "6", replies = listOf(
                                                createCommentView(
                                                    id = "15", parentId = "12", replies = listOf(
                                                        createCommentView(
                                                            id = "16",
                                                            parentId = "15",
                                                            replies = null
                                                        )
                                                    )
                                                )
                                            )
                                        ),
                                        createCommentView(
                                            id = "13",
                                            parentId = "6",
                                            replies = null
                                        ),
                                        createCommentView(
                                            id = "14", parentId = "6", replies = listOf(
                                                clickedComment,
                                                createCommentView(
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
                createCommentView(
                    id = "1", parentId = null, replies = listOf(
                        createCommentView(
                            id = "2", parentId = "1", replies = listOf(
                                createCommentView(
                                    id = "5", parentId = "2", replies = listOf(
                                        createCommentView(
                                            id = "7", parentId = "5", replies = listOf(
                                                createCommentView(
                                                    id = "8",
                                                    parentId = "7",
                                                    replies = null
                                                )
                                            )
                                        )
                                    )
                                ),
                                createCommentView(
                                    id = "6", parentId = "2", replies = listOf(
                                        createCommentView(
                                            id = "12", parentId = "6", replies = listOf(
                                                createCommentView(
                                                    id = "15", parentId = "12", replies = listOf(
                                                        createCommentView(
                                                            id = "16",
                                                            parentId = "15",
                                                            replies = null
                                                        )
                                                    )
                                                )
                                            )
                                        ),
                                        createCommentView(
                                            id = "13",
                                            parentId = "6",
                                            replies = null
                                        ),
                                        createCommentView(
                                            id = "14", parentId = "6", replies = listOf(
                                                clickedComment.copy(isCollapsed = true),
                                                createCommentView(
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
            val clickedComment = createCommentView(
                id = "10", parentId = null, replies = listOf(
                    createCommentView(id = "11", parentId = "10", replies = null)
                )
            )
            val allComments = listOf(
                createCommentView(
                    id = "1", parentId = null, replies = listOf(
                        createCommentView(
                            id = "2", parentId = "1", replies = listOf(
                                createCommentView(
                                    id = "5", parentId = "2", replies = listOf(
                                        createCommentView(
                                            id = "7", parentId = "5", replies = listOf(
                                                createCommentView(
                                                    id = "8",
                                                    parentId = "7",
                                                    replies = null
                                                )
                                            )
                                        )
                                    )
                                ),
                                createCommentView(
                                    id = "6", parentId = "2", replies = listOf(
                                        createCommentView(
                                            id = "12", parentId = "6", replies = listOf(
                                                createCommentView(
                                                    id = "15", parentId = "12", replies = listOf(
                                                        createCommentView(
                                                            id = "16",
                                                            parentId = "15",
                                                            replies = null
                                                        )
                                                    )
                                                )
                                            )
                                        ),
                                        createCommentView(
                                            id = "13",
                                            parentId = "6",
                                            replies = null
                                        ),
                                        createCommentView(
                                            id = "14", parentId = "6", replies = listOf(
                                                createCommentView(
                                                    id = "17",
                                                    parentId = "14",
                                                    replies = null
                                                ),
                                                createCommentView(
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
                        createCommentView(id = "3", parentId = "1", replies = null),
                        createCommentView(id = "4", parentId = "1", replies = null)
                    )
                ),
                createCommentView(id = "9", parentId = null, replies = null),
                clickedComment
            )
            every { view.showComments(any()) } just runs

            //Act
            presenter.onCommentLongClicked(clickedComment, allComments)

            //Assert
            val expectedComments = listOf(
                createCommentView(
                    id = "1", parentId = null, replies = listOf(
                        createCommentView(
                            id = "2", parentId = "1", replies = listOf(
                                createCommentView(
                                    id = "5", parentId = "2", replies = listOf(
                                        createCommentView(
                                            id = "7", parentId = "5", replies = listOf(
                                                createCommentView(
                                                    id = "8",
                                                    parentId = "7",
                                                    replies = null
                                                )
                                            )
                                        )
                                    )
                                ),
                                createCommentView(
                                    id = "6", parentId = "2", replies = listOf(
                                        createCommentView(
                                            id = "12", parentId = "6", replies = listOf(
                                                createCommentView(
                                                    id = "15", parentId = "12", replies = listOf(
                                                        createCommentView(
                                                            id = "16",
                                                            parentId = "15",
                                                            replies = null
                                                        )
                                                    )
                                                )
                                            )
                                        ),
                                        createCommentView(
                                            id = "13",
                                            parentId = "6",
                                            replies = null
                                        ),
                                        createCommentView(
                                            id = "14", parentId = "6", replies = listOf(
                                                createCommentView(
                                                    id = "17",
                                                    parentId = "14",
                                                    replies = null
                                                ),
                                                createCommentView(
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
                        createCommentView(id = "3", parentId = "1", replies = null),
                        createCommentView(id = "4", parentId = "1", replies = null)
                    )
                ),
                createCommentView(id = "9", parentId = null, replies = null),
                clickedComment.copy(isCollapsed = true)
            )
            verify { view.showComments(expectedComments) }
        }

        @Test
        fun `handle collapsing nested comment of other root comments`() {
            //Arrange
            val clickedComment = createCommentView(id = "11", parentId = "10", replies = null)
            val allComments = listOf(
                createCommentView(
                    id = "1", parentId = null, replies = listOf(
                        createCommentView(
                            id = "2", parentId = "1", replies = listOf(
                                createCommentView(
                                    id = "5", parentId = "2", replies = listOf(
                                        createCommentView(
                                            id = "7", parentId = "5", replies = listOf(
                                                createCommentView(
                                                    id = "8",
                                                    parentId = "7",
                                                    replies = null
                                                )
                                            )
                                        )
                                    )
                                ),
                                createCommentView(
                                    id = "6", parentId = "2", replies = listOf(
                                        createCommentView(
                                            id = "12", parentId = "6", replies = listOf(
                                                createCommentView(
                                                    id = "15", parentId = "12", replies = listOf(
                                                        createCommentView(
                                                            id = "16",
                                                            parentId = "15",
                                                            replies = null
                                                        )
                                                    )
                                                )
                                            )
                                        ),
                                        createCommentView(
                                            id = "13",
                                            parentId = "6",
                                            replies = null
                                        ),
                                        createCommentView(
                                            id = "14", parentId = "6", replies = listOf(
                                                createCommentView(
                                                    id = "17",
                                                    parentId = "14",
                                                    replies = null
                                                ),
                                                createCommentView(
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
                        createCommentView(id = "3", parentId = "1", replies = null),
                        createCommentView(id = "4", parentId = "1", replies = null)
                    )
                ),
                createCommentView(id = "9", parentId = null, replies = null),
                createCommentView(
                    id = "10", parentId = null, replies = listOf(clickedComment)
                )
            )
            every { view.showComments(any()) } just runs

            //Act
            presenter.onCommentLongClicked(clickedComment, allComments)

            //Assert
            val expectedComment = listOf(
                createCommentView(
                    id = "1", parentId = null, replies = listOf(
                        createCommentView(
                            id = "2", parentId = "1", replies = listOf(
                                createCommentView(
                                    id = "5", parentId = "2", replies = listOf(
                                        createCommentView(
                                            id = "7", parentId = "5", replies = listOf(
                                                createCommentView(
                                                    id = "8",
                                                    parentId = "7",
                                                    replies = null
                                                )
                                            )
                                        )
                                    )
                                ),
                                createCommentView(
                                    id = "6", parentId = "2", replies = listOf(
                                        createCommentView(
                                            id = "12", parentId = "6", replies = listOf(
                                                createCommentView(
                                                    id = "15", parentId = "12", replies = listOf(
                                                        createCommentView(
                                                            id = "16",
                                                            parentId = "15",
                                                            replies = null
                                                        )
                                                    )
                                                )
                                            )
                                        ),
                                        createCommentView(
                                            id = "13",
                                            parentId = "6",
                                            replies = null
                                        ),
                                        createCommentView(
                                            id = "14", parentId = "6", replies = listOf(
                                                createCommentView(
                                                    id = "17",
                                                    parentId = "14",
                                                    replies = null
                                                ),
                                                createCommentView(
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
                        createCommentView(id = "3", parentId = "1", replies = null),
                        createCommentView(id = "4", parentId = "1", replies = null)
                    )
                ),
                createCommentView(id = "9", parentId = null, replies = null),
                createCommentView(
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
            val commentView = createCommentView(isCollapsed = true)
            every { view.showComments(any()) } just runs

            //Act
            presenter.onCommentLongClicked(commentView, listOf(commentView))

            //Assert
            verify { view.showComments(listOf(commentView.copy(isCollapsed = false))) }
        }

        @Test
        fun `handle expanding deeply nested comments`() {
            //Arrange
            val clickedComment = createCommentView(
                id = "16",
                parentId = "15",
                replies = null,
                isCollapsed = true
            )
            val allComments = listOf(
                createCommentView(
                    id = "1", parentId = null, replies = listOf(
                        createCommentView(
                            id = "2", parentId = "1", replies = listOf(
                                createCommentView(
                                    id = "5", parentId = "2", replies = listOf(
                                        createCommentView(
                                            id = "7", parentId = "5", replies = listOf(
                                                createCommentView(
                                                    id = "8",
                                                    parentId = "7",
                                                    replies = null
                                                )
                                            )
                                        )
                                    )
                                ),
                                createCommentView(
                                    id = "6", parentId = "2", replies = listOf(
                                        createCommentView(
                                            id = "12", parentId = "6", replies = listOf(
                                                createCommentView(
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
                createCommentView(
                    id = "1", parentId = null, replies = listOf(
                        createCommentView(
                            id = "2", parentId = "1", replies = listOf(
                                createCommentView(
                                    id = "5", parentId = "2", replies = listOf(
                                        createCommentView(
                                            id = "7", parentId = "5", replies = listOf(
                                                createCommentView(
                                                    id = "8",
                                                    parentId = "7",
                                                    replies = null
                                                )
                                            )
                                        )
                                    )
                                ),
                                createCommentView(
                                    id = "6", parentId = "2", replies = listOf(
                                        createCommentView(
                                            id = "12", parentId = "6", replies = listOf(
                                                createCommentView(
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
    }
}