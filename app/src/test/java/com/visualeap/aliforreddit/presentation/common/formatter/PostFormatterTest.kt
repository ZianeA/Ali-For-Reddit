package com.visualeap.aliforreddit.presentation.common.formatter

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.Assert.*
import org.junit.Test
import util.domain.createPost
import util.domain.createPostDto
import util.domain.createSubreddit

class PostFormatterTest {
    @Test
    fun `format post`() {
        // Act
        val formattedPost = PostFormatter.formatPost(createSubreddit(), createPost())

        // Assert
        assertThat(formattedPost).isEqualTo(createPostDto())
    }
}