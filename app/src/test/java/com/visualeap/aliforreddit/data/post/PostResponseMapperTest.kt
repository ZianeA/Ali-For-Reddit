package com.visualeap.aliforreddit.data.post

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import util.domain.createPost
import util.domain.createPostResponse

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class PostResponseMapperTest {
    @Test
    fun `map PostResponse to Post`() {
        //Act
        val mappedPostList = PostResponseMapper.map(createPostResponse())

        //Assert
        Assertions.assertThat(mappedPostList).isEqualTo(listOf(createPost()))
    }
}