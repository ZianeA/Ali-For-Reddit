package com.visualeap.aliforreddit.data.network

import com.visualeap.aliforreddit.domain.usecase.AuthService
import com.visualeap.aliforreddit.domain.usecase.RefreshToken
import com.visualeap.aliforreddit.domain.util.HttpHeaders
import com.visualeap.aliforreddit.util.createResponse
import com.visualeap.aliforreddit.util.createToken
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import okhttp3.Request
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TokenAuthenticatorTest {

    private val refreshToken: RefreshToken = mockk()
    private val authService: AuthService = mockk()
    private val authenticator = TokenAuthenticator(authService, refreshToken)

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
    }

    @Nested
    inner class Authenticate {

        @Test
        fun `add refreshed access token to request`() {
            //Arrange
            val response = createResponse()
            val token = createToken()
            every { refreshToken.execute(Unit) } returns token

            //Act
            val request = authenticator.authenticate(null, response)

            //Assert
            val header = request?.header(HttpHeaders.AUTHORIZATION)
            assertThat(header)
                .isEqualTo("${token.type} ${token.accessToken}")
        }

        @Test
        fun `attempt to authenticate only once`() {
            //Arrange
            var response = createResponse()
            var request: Request? = null
            every { refreshToken.execute(Unit) } returns createToken()

            //Act
            repeat(2) {
                request = authenticator.authenticate(null, response)
                //Set response with the return request
                request?.let { response = createResponse(it) }
            }

            //Assert
            verify(atMost = 1) { refreshToken.execute(Unit) }
            assertThat(request).isNull()
        }

        @Test
        fun `attempt to authenticate only once (repeat times = 3)`() {
            //Arrange
            var response = createResponse()
            var request: Request? = null
            every { refreshToken.execute(Unit) } returns createToken()

            //Act
            repeat(3) {
                request = authenticator.authenticate(null, response)
                //Set the inserted response with the returned request
                request?.let { response = createResponse(it) }
            }

            //Assert
            verify(atMost = 1) { refreshToken.execute(Unit) }
            assertThat(request).isNull()
        }

        @Test
        fun `return null when new token is null`() {
            //Arrange
            val response = createResponse()
            every { refreshToken.execute(Unit) } returns null

            //Act
            val request = authenticator.authenticate(null, response)

            //Assert
            assertThat(request).isNull()
        }
    }
}