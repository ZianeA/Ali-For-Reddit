package com.visualeap.aliforreddit.data.repository.comment

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import util.domain.createComment
import util.domain.createCommentResponse

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class CommentResponseMapperTest{
    private val mapper = CommentResponseMapper()

    @Test
    fun `map CommentResponse to a list of Comment`() {
        //Act
        val mappedCommentList = mapper.map(createCommentResponse())

        //Assert
        assert(mappedCommentList == listOf(createComment()))
    }

    @Test
    fun `map CommentResponse when it has no replies`() {
        //Act
        val mappedCommentList = mapper.map(createCommentResponse(replies = null))

        //Assert
        assert(mappedCommentList == listOf(createComment(replies = null)))
    }
}