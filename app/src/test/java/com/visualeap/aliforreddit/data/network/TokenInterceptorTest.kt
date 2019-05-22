package com.visualeap.aliforreddit.data.network

import com.visualeap.aliforreddit.domain.usecase.GetToken
import com.visualeap.aliforreddit.domain.util.HttpHeaders
import com.visualeap.aliforreddit.util.createRequest
import com.visualeap.aliforreddit.util.createResponse
import com.visualeap.aliforreddit.util.createToken
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import okhttp3.Interceptor
import okhttp3.Request
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TokenInterceptorTest {

    private val chain: Interceptor.Chain = mockk()
    private val getAccessToken: GetToken = mockk()
    private val tokenInterceptor = TokenInterceptor(getAccessToken)

    @Nested
    inner class Intercept {
        @Test
        fun `add bearer token to request`() {
            //Arrange
            val token = createToken()
            val requestSlot = slot<Request>()
            val request = createRequest()

            every { chain.request() } returns request
            every { chain.proceed(capture(requestSlot)) } answers { createResponse(requestSlot.captured) }
            every { getAccessToken.execute(Unit) } returns token

            //Act
            val response = tokenInterceptor.intercept(chain)

            //Assert
            val header = response.request().header(HttpHeaders.AUTHORIZATION)
            assertThat(header).isEqualTo("${token.type} ${token.accessToken}")
        }
    }
}
