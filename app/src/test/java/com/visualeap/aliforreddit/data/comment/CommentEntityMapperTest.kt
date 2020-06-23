package com.visualeap.aliforreddit.data.comment

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import util.domain.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class CommentEntityMapperTest {
    private val mapper = CommentEntityMapper()

    @Nested
    inner class Map {
        @Test
        fun `map a list of CommentEntity to a list of Comment`() {
            //Arrange
            val commentEntityList = listOf(
                createCommentEntity(id = COMMENT_ID, parentId = null),
                createCommentEntity(
                    id = NESTED_COMMENT_ID,
                    parentId = COMMENT_ID,
                    depth = NESTED_COMMENT_DEPTH
                )
            )

            //Act
            val mappedComment = mapper.map(commentEntityList)

            //Assert
            assertThat(mappedComment).isEqualTo(listOf(createComment()))
        }

        @Test
        fun `handle a deeply nested list of CommentEntity`() {
            //Arrange
            val commentEntityList = listOf(
                createCommentEntity(id = "1", parentId = null),
                createCommentEntity(id = "2", parentId = "1"),
                createCommentEntity(id = "5", parentId = "2"),
                createCommentEntity(id = "6", parentId = "2"),
                createCommentEntity(id = "12", parentId = "6"),
                createCommentEntity(id = "13", parentId = "6"),
                createCommentEntity(id = "14", parentId = "6"),
                createCommentEntity(id = "15", parentId = "12"),
                createCommentEntity(id = "16", parentId = "15"),
                createCommentEntity(id = "17", parentId = "14"),
                createCommentEntity(id = "18", parentId = "14"),
                createCommentEntity(id = "7", parentId = "5"),
                createCommentEntity(id = "8", parentId = "7"),
                createCommentEntity(id = "3", parentId = "1"),
                createCommentEntity(id = "4", parentId = "1"),
                createCommentEntity(id = "9", parentId = null),
                createCommentEntity(id = "10", parentId = null),
                createCommentEntity(id = "11", parentId = "10")
            )

            //Act
            val mappedCommentList = mapper.map(commentEntityList)

            //Assert
            val expectedCommentList = listOf(
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
            assertThat(mappedCommentList).isEqualTo(expectedCommentList)
        }
    }

    @Nested
    inner class ReverseMap {
        @Test
        fun `map a list of Comment to a list of CommentEntity`() {
            //Act
            val mappedCommentEntityList = mapper.mapReverse(listOf(createComment()))

            //Assert
            val expectedCommentEntityList = listOf(
                createCommentEntity(),
                createCommentEntity(
                    id = NESTED_COMMENT_ID,
                    parentId = COMMENT_ID,
                    depth = NESTED_COMMENT_DEPTH
                )
            )
            assertThat(mappedCommentEntityList).isEqualTo(expectedCommentEntityList)
        }

        @Test
        fun `handle deeply nested replies`() {
            //Arrange
            val deeplyNestedCommentList = listOf(
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
            val mappedCommentEntityList = mapper.mapReverse(deeplyNestedCommentList)

            //Assert
            val expectedCommentEntityList = listOf(
                createCommentEntity(id = "1", parentId = null),
                createCommentEntity(id = "2", parentId = "1"),
                createCommentEntity(id = "5", parentId = "2"),
                createCommentEntity(id = "6", parentId = "2"),
                createCommentEntity(id = "12", parentId = "6"),
                createCommentEntity(id = "13", parentId = "6"),
                createCommentEntity(id = "14", parentId = "6"),
                createCommentEntity(id = "15", parentId = "12"),
                createCommentEntity(id = "16", parentId = "15"),
                createCommentEntity(id = "17", parentId = "14"),
                createCommentEntity(id = "18", parentId = "14"),
                createCommentEntity(id = "7", parentId = "5"),
                createCommentEntity(id = "8", parentId = "7"),
                createCommentEntity(id = "3", parentId = "1"),
                createCommentEntity(id = "4", parentId = "1"),
                createCommentEntity(id = "9", parentId = null),
                createCommentEntity(id = "10", parentId = null),
                createCommentEntity(id = "11", parentId = "10")
            )

            //The order shouldn't matter
            assertThat(mappedCommentEntityList).containsExactlyInAnyOrderElementsOf(
                expectedCommentEntityList
            )
        }
    }

}