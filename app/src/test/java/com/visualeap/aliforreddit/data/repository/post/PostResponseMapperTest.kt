package com.visualeap.aliforreddit.data.repository.post

import com.visualeap.aliforreddit.domain.model.Post
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.math.exp

//TODO implement
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class PostResponseMapperTest {
    private val mapper = PostResponseMapper()

    /*@Test
    fun `map PostResponse to Post`() {
        //Arrange
        *//*val post = PostResponse.Data.PostHolder.Post(
            "AnyId",
            "AnyAuthorName",
            "AnyTitle",
            "AnyText",
            101,
            201
        )*//*
        val postHolder = PostResponse.Data.PostHolder(post)
        val postResponse =
            PostResponse(PostResponse.Data("TestKey", listOf(postHolder, postHolder, postHolder)))

        //Act
        val actualPostList = mapper.map(postResponse)

        //Assert
        *//*val expectedPost = post.run { Post(id, authorName, title, text, score, commentCount) }
        val expectedPostList = listOf(expectedPost, expectedPost, expectedPost)
        assert(actualPostList == expectedPostList)*//*
    }*/
}