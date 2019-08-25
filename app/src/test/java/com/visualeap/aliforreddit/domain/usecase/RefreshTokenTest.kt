package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.domain.repository.TokenRepository
import io.mockk.*
import io.reactivex.Maybe
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import util.domain.createUserToken
import util.domain.createUserlessToken

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class RefreshTokenTest {
    private val tokenRepository: TokenRepository = mockk()
    private val refreshToken = RefreshToken(tokenRepository)

    companion object {
        private const val REFRESHED_ACCESS_TOKEN = "ACCESS TOKEN HAS BEEN REFRESHED"
    }

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
    }

    @Test
    fun `refresh user-token when user is logged in`() {
        //Arrange
        val currentToken = createUserToken()
        val refreshedUserToken =
            createUserToken(accessToken = REFRESHED_ACCESS_TOKEN)

        every { tokenRepository.getCurrentToken() } returns Maybe.just(currentToken)
        every {
            tokenRepository.refreshUserToken(currentToken.id, currentToken.refreshToken)
        } returns Single.just(refreshedUserToken)

        //Act, Assert
        refreshToken.execute(Unit)
            .test()
            .assertResult(refreshedUserToken)
    }

    @Test
    fun `refresh user-less token when no user is logged in`() {
        //Arrange
        val currentToken = createUserlessToken()
        val refreshedUserLessToken =
            createUserlessToken(accessToken = REFRESHED_ACCESS_TOKEN)

        every { tokenRepository.getCurrentToken() } returns Maybe.just(currentToken)
        every { tokenRepository.refreshUserlessToken(currentToken.deviceId) } returns Single.just(
            refreshedUserLessToken
        )

        //Act, Assert
        refreshToken.execute(Unit)
            .test()
            .assertResult(refreshedUserLessToken)
    }

    @Test
    fun `throw exception when no token is currently in use`() {
        //Arrange
        every { tokenRepository.getCurrentToken() } returns Maybe.empty()

        //Act, Assert
        refreshToken.execute(Unit)
            .test()
            .assertError(NoSuchElementException::class.java)
    }
}

