package com.visualeap.aliforreddit.data.network

import com.visualeap.aliforreddit.data.network.auth.TokenInterceptor
import com.visualeap.aliforreddit.domain.usecase.GetToken
import com.visualeap.aliforreddit.domain.util.HttpHeaders
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.reactivex.Single
import okhttp3.Interceptor
import okhttp3.Request
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import util.domain.createRequest
import util.domain.createResponse
import util.domain.createToken

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TokenInterceptorTest {

    private val chain: Interceptor.Chain = mockk()
    private val getToken: GetToken = mockk()
    private val tokenInterceptor =
        TokenInterceptor(getToken)

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
    }

    @Nested
    inner class Intercept {
        @Test
        fun `add bearer token to request`() {
            //Arrange
            val token = createToken()
            val requestSlot = slot<Request>()
            val request = createRequest()

            every { chain.request() } returns request
            every { chain.proceed(capture(requestSlot)) } answers {
                createResponse(
                    requestSlot.captured
                )
            }
            every { getToken.execute(Unit) } returns Single.just(token)

            //Act
            val response = tokenInterceptor.intercept(chain)

            //Assert
            val header = response.request().header(HttpHeaders.AUTHORIZATION)
            assertThat(header).isEqualTo("${token.type} ${token.accessToken}")
        }

        @Test
        fun `ignore when token is null`() {
            //Arrange
            val request = createRequest()
            val requestSlot = slot<Request>()
            every { chain.request() } returns request
            every { chain.proceed(capture(requestSlot)) } answers {
                createResponse(
                    requestSlot.captured
                )
            }
            every { getToken.execute(Unit) } returns Single.error(Throwable())

            //Act
            val response = tokenInterceptor.intercept(chain)

            //Assert
            assertThat(response.request()).isEqualTo(request)
        }
    }
}
