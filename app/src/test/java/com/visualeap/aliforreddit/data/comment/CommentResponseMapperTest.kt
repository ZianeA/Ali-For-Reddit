package com.visualeap.aliforreddit.data.comment

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import util.domain.createComment
import util.domain.createCommentResponse

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class CommentResponseMapperTest{
    @Test
    fun `map CommentResponse to a list of Comment`() {
        //Act
        val mappedCommentList = CommentResponseMapper.map(createCommentResponse())

        //Assert
        assert(mappedCommentList == listOf(createComment()))
    }

    @Test
    fun `map CommentResponse when it has no replies`() {
        //Act
        val mappedCommentList = CommentResponseMapper.map(createCommentResponse(replies = null))

        //Assert
        assert(mappedCommentList == listOf(createComment(replies = null)))
    }
}