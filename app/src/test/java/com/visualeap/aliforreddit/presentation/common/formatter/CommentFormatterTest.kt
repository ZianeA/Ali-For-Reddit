package com.visualeap.aliforreddit.presentation.common.formatter

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import util.domain.createComment
import util.domain.createCommentDto

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class CommentFormatterTest {
    @Test
    fun `map List of Comment to List of CommentView`() {
        //Act
        val mappedCommentViewList = CommentFormatter.format(listOf(createComment()))

        //Assert
        Assertions.assertThat(mappedCommentViewList).isEqualTo(listOf(createCommentDto()))
    }

    @Test
    fun `format deeply nested list of Comment`() {
        //Arrange
        val commentList = listOf(
            createComment(
                id = "1", parentId = null, replies = listOf(
                    createComment(
                        id = "2", parentId = "1", replies = listOf(
                            createComment(
                                id = "5", parentId = "2", replies = listOf(
                                    createComment(
                                        id = "7", parentId = "5", replies = listOf(
                                            createComment(
                                                id = "8",
                                                parentId = "7",
                                                replies = null
                                            )
                                        )
                                    )
                                )
                            ),
                            createComment(
                                id = "6", parentId = "2", replies = listOf(
                                    createComment(
                                        id = "12", parentId = "6", replies = listOf(
                                            createComment(
                                                id = "15", parentId = "12", replies = listOf(
                                                    createComment(
                                                        id = "16",
                                                        parentId = "15",
                                                        replies = null
                                                    )
                                                )
                                            )
                                        )
                                    ),
                                    createComment(id = "13", parentId = "6", replies = null),
                                    createComment(
                                        id = "14", parentId = "6", replies = listOf(
                                            createComment(
                                                id = "17",
                                                parentId = "14",
                                                replies = null
                                            ),
                                            createComment(
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
                    createComment(id = "3", parentId = "1", replies = null),
                    createComment(id = "4", parentId = "1", replies = null)
                )
            ),
            createComment(id = "9", parentId = null, replies = null),
            createComment(
                id = "10", parentId = null, replies = listOf(
                    createComment(id = "11", parentId = "10", replies = null)
                )
            )
        )

        //Act
        val mappedCommentViewList = CommentFormatter.format(commentList)

        //Assert
        val expectedCommentViewList = listOf(
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
                                    createCommentDto(id = "13", parentId = "6", replies = null),
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
                    createCommentDto(id = "4", parentId = "1", replies = null, isLastReply = true)
                )
            ),
            createCommentDto(id = "9", parentId = null, replies = null, isLastReply = true),
            createCommentDto(
                id = "10", parentId = null, replies = listOf(
                    createCommentDto(
                        id = "11",
                        parentId = "10",
                        replies = null,
                        isLastReply = true
                    )
                )
            )
        )
        Assertions.assertThat(mappedCommentViewList).isEqualTo(expectedCommentViewList)
    }

    @Test
    fun `find last reply for deeply nested list of comments`() {
        //Arrange
        val commentList = listOf(
            createComment(
                id = "1", parentId = null, replies = listOf(
                    createComment(
                        id = "2", parentId = "1", replies = listOf(
                            createComment(
                                id = "5", parentId = "2", replies = listOf(
                                    createComment(
                                        id = "7", parentId = "5", replies = listOf(
                                            createComment(
                                                id = "8",
                                                parentId = "7",
                                                replies = null
                                            )
                                        )
                                    )
                                )
                            ),
                            createComment(
                                id = "6", parentId = "2", replies = listOf(
                                    createComment(
                                        id = "12", parentId = "6", replies = listOf(
                                            createComment(
                                                id = "15", parentId = "12", replies = listOf(
                                                    createComment(
                                                        id = "16",
                                                        parentId = "15",
                                                        replies = null
                                                    )
                                                )
                                            )
                                        )
                                    ),
                                    createComment(id = "13", parentId = "6", replies = null),
                                    createComment(
                                        id = "14", parentId = "6", replies = listOf(
                                            createComment(
                                                id = "17",
                                                parentId = "14",
                                                replies = null
                                            ),
                                            createComment(
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

        //Act
        val mappedCommentViewList = CommentFormatter.format(commentList)

        //Assert
        val expectedCommentViewList = listOf(
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
                                    createCommentDto(id = "13", parentId = "6", replies = null),
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
                                                replies = null,
                                                isLastReply = true
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
        Assertions.assertThat(mappedCommentViewList).isEqualTo(expectedCommentViewList)
    }
}