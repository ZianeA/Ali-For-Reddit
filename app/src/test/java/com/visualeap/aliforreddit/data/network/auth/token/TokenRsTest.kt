package com.visualeap.aliforreddit.data.network.auth.token

import com.visualeap.aliforreddit.data.network.auth.AuthService
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import util.domain.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
internal class TokenRsTest {
    private val authService: AuthService = mockk()
    private val basicAuth = createBasicAuth()
    private val tokenRs = TokenRs(authService, REDIRECT_URL, basicAuth)

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
    }

    @Test
    fun `return user token`() {
        //Arrange
        val tokenResponse = createTokenResponse()
        every {
            authService.getUserToken(
                any(),
                CODE,
                REDIRECT_URL,
                basicAuth
            )
        } returns Single.just(tokenResponse)

        //Act, Assert
        val expectedUserToken = createUserToken(
            NOT_SET_ROW_ID,
            tokenResponse.accessToken,
            tokenResponse.type,
            tokenResponse.refreshToken!!
        )
        tokenRs.getUserToken(CODE)
            .test()
            .assertResult(expectedUserToken)
    }

    @Test
    fun `refresh user token`() {
        //Arrange
        val tokenResponse = createTokenResponse(refreshToken = null)
        every { authService.refreshUserToken(any(), REFRESH_TOKEN) } returns Single.just(
            tokenResponse
        )

        //Act, Assert
        val expectedUserToken = createUserToken(
            NOT_SET_ROW_ID,
            tokenResponse.accessToken,
            tokenResponse.type,
            REFRESH_TOKEN
        )
        tokenRs.refreshUserToken(REFRESH_TOKEN)
            .test()
            .assertResult(expectedUserToken)
    }

    @Test
    fun `return user-less token`() {
        //Arrange
        val tokenResponse = createTokenResponse(refreshToken = null)
        every { authService.getUserlessToken(any(), DEVICE_ID, basicAuth) } returns Single.just(
            tokenResponse
        )

        //Act, Assert
        val expectedToken = createUserlessToken(
            NOT_SET_ROW_ID,
            tokenResponse.accessToken,
            tokenResponse.type,
            DEVICE_ID
        )
        tokenRs.getUserlessToken(DEVICE_ID)
            .test()
            .assertResult(expectedToken)
    }

    private fun createTokenResponse(
        accessToken: String = ACCESS_TOKEN,
        type: String = TOKEN_TYPE,
        refreshToken: String? = REFRESH_TOKEN
    ) =
        TokenResponse(accessToken, type, refreshToken)
}