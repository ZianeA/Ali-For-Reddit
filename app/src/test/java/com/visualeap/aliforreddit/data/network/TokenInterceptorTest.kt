package com.visualeap.aliforreddit.data.network

import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.data.network.auth.TokenInterceptor
import com.visualeap.aliforreddit.domain.usecase.FetchToken
import com.visualeap.aliforreddit.domain.util.HttpHeaders
import com.visualeap.aliforreddit.presentation.common.ResourceProvider
import com.visualeap.aliforreddit.util.createMockChain
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
import util.domain.createToken

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TokenInterceptorTest {
    private val fetchToken: FetchToken = mockk()
    private val tokenInterceptor = TokenInterceptor(fetchToken)
    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
    }

    @Nested
    inner class Intercept {
        @Test
        fun `when token is not null should add it to request`() {
            //Arrange
            val token = createToken()
            every { fetchToken.execute() } returns Single.just(token)

            //Act
            val response = tokenInterceptor.intercept(createMockChain())

            //Assert
            val header = response.request().header(HttpHeaders.AUTHORIZATION)
            assertThat(header).isEqualTo("${token.type} ${token.accessToken}")
        }

        @Test
        fun `when token retrieval fails should leave request as is`() {
            //Arrange
            val chain = createMockChain()
            every { fetchToken.execute() } returns Single.error(Throwable())

            //Act
            val response = tokenInterceptor.intercept(chain)

            //Assert
            assertThat(response.request()).isEqualTo(chain.request())
        }
    }
}
