package com.visualeap.aliforreddit.data.network

import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.data.network.auth.TokenAuthenticator
import com.visualeap.aliforreddit.domain.usecase.RefreshToken
import com.visualeap.aliforreddit.domain.util.HttpHeaders
import com.visualeap.aliforreddit.presentation.common.ResourceProvider
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import okhttp3.Request
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import util.domain.createRequest
import util.domain.createResponse
import util.domain.createToken

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TokenAuthenticatorTest {
    private val refreshToken: RefreshToken = mockk()
    private val authenticator = TokenAuthenticator(refreshToken)

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
    }

    @Nested
    inner class Authenticate {
        @Test
        fun `when token is not null should add it request`() {
            //Arrange
            val token = createToken()
            every { refreshToken.execute() } returns Single.just(token)

            //Act
            val request = authenticator.authenticate(null, createResponse())

            //Assert
            val header = request!!.header(HttpHeaders.AUTHORIZATION)
            assertThat(header)
                .isEqualTo("${token.type} ${token.accessToken}")
        }

        @Test
        fun `when token retrieval fails should leave request as is`() {
            //Arrange
            every { refreshToken.execute() } returns Single.error(Throwable())

            //Act
            val request = authenticator.authenticate(null, createResponse())

            //Assert
            assertThat(request).isNull()
        }

        @ParameterizedTest
        @ValueSource(ints = [2, 3])
        fun `should attempt to authenticate only once`(time: Int) {
            //Arrange
            var response = createResponse()
            var request: Request? = null
            every { refreshToken.execute() } returns Single.just(createToken())

            //Act
            repeat(time) {
                request = authenticator.authenticate(null, response)
                // Add the previous request to the new response
                request?.let { response = createResponse(it) }
            }

            //Assert
            assertThat(request).isNull()
        }
    }
}