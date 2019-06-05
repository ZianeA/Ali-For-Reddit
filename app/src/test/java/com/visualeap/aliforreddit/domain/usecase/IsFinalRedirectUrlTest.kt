package com.visualeap.aliforreddit.domain.usecase

import okhttp3.HttpUrl
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IsFinalRedirectUrlTest {

    companion object {
        private const val REDIRECT_URL = "https://example.com/path"
    }

    private val isFinalRedirectUrl = IsFinalRedirectUrl(REDIRECT_URL)

    @Test
    fun `return true when final url contains redirect url`() {
        //Arrange
        val finalUrl = HttpUrl.parse(REDIRECT_URL)!!
            .newBuilder()
            .addQueryParameter("query", "test")
            .build()
            .toString()

        //Act
        val actual = isFinalRedirectUrl.execute(finalUrl)

        //Assert
        assertThat(actual).isEqualTo(true)
    }

    @Test
    fun `return false when final url doesn't contain redirect url`() {
        //Arrange
        val finalUrl = "https://invalid.com"

        //Act
        val actual = isFinalRedirectUrl.execute(finalUrl)

        //Assert
        assertThat(actual).isEqualTo(false)
    }

    @Test
    fun `return false when final url is malformed`() {
        //Arrange
        val finalUrl = "this is a malformed URL $REDIRECT_URL"

        //Act
        val actual = isFinalRedirectUrl.execute(finalUrl)

        //Assert
        assertThat(actual).isEqualTo(false)
    }

    @Test
    fun `return false when final url doesn't contain any query`() {
        //Arrange
        val finalUrl = REDIRECT_URL

        //Act
        val actual = isFinalRedirectUrl.execute(finalUrl)

        //Assert
        assertThat(actual).isEqualTo(false)
    }
}