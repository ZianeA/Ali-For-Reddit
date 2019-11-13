package com.visualeap.aliforreddit.presentation.mapper

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import util.domain.createComment
import util.domain.createCommentView

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class CommentViewMapperTest {
    private val mapper = CommentViewMapper()

    @Test
    fun `map List of Comment to List of CommentView`() {
        //Act
        val mappedCommentViewList = mapper.mapReverse(listOf(createComment()))

        //Assert
        Assertions.assertThat(mappedCommentViewList).isEqualTo(listOf(createCommentView()))
    }

    @Test
    fun `handle deeply nested list of Comment`() {
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
        val mappedCommentViewList = mapper.mapReverse(commentList)

        //Assert
        val expectedCommentViewList = listOf(
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
                                    createCommentView(id = "13", parentId = "6", replies = null),
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
                    createCommentView(id = "4", parentId = "1", replies = null, isLastReply = true)
                )
            ),
            createCommentView(id = "9", parentId = null, replies = null, isLastReply = true),
            createCommentView(
                id = "10", parentId = null, replies = listOf(
                    createCommentView(
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
        val mappedCommentViewList = mapper.mapReverse(commentList)

        //Assert
        val expectedCommentViewList = listOf(
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
                                    createCommentView(id = "13", parentId = "6", replies = null),
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