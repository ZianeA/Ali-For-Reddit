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

    private val isFinalRedirectUrl = IsFinalRedirectUrl()

    @Test
    fun `return true when final url contains redirect url`() {
        //Arrange
        val finalUrl = HttpUrl.parse(REDIRECT_URL)!!
            .newBuilder()
            .addQueryParameter("query", "test")
            .build()
            .toString()

        val params = IsFinalRedirectUrl.Params(REDIRECT_URL, finalUrl)

        //Act
        val actual = isFinalRedirectUrl.execute(params)

        //Assert
        assertThat(actual).isEqualTo(true)
    }

    @Test
    fun `return false when final url doesn't contain redirect url`() {
        //Arrange
        val finalUrl = "https://invalid.com"
        val params = IsFinalRedirectUrl.Params(REDIRECT_URL, finalUrl)

        //Act
        val actual = isFinalRedirectUrl.execute(params)

        //Assert
        assertThat(actual).isEqualTo(false)
    }

    @Test
    fun `return false when final url is malformed`() {
        //Arrange
        val finalUrl = "this is a malformed URL $REDIRECT_URL"
        val params = IsFinalRedirectUrl.Params(REDIRECT_URL, finalUrl)

        //Act
        val actual = isFinalRedirectUrl.execute(params)

        //Assert
        assertThat(actual).isEqualTo(false)
    }

    @Test
    fun `return false when final url doesn't contain any query`() {
        //Arrange
        val finalUrl = REDIRECT_URL
        val params = IsFinalRedirectUrl.Params(REDIRECT_URL, finalUrl)

        //Act
        val actual = isFinalRedirectUrl.execute(params)

        //Assert
        assertThat(actual).isEqualTo(false)
    }
}