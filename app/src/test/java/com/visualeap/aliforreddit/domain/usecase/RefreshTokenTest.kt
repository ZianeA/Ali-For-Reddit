package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.data.network.auth.AuthService
import com.visualeap.aliforreddit.domain.util.BasicAuthCredentialProvider
import com.visualeap.aliforreddit.domain.model.token.UserToken
import com.visualeap.aliforreddit.domain.model.token.UserlessToken
import com.visualeap.aliforreddit.domain.repository.TokenRepository
import com.visualeap.aliforreddit.domain.util.TokenResponseMapper
import io.mockk.*
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import util.domain.createTokenResponse
import util.domain.createUserToken
import util.domain.createUserlessToken
import util.domain.match
import java.lang.Exception

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class RefreshTokenTest {
    private val authService: AuthService = mockk()
    private val tokenRepository: TokenRepository = mockk()
    private val authCredentialProvider: BasicAuthCredentialProvider = mockk(relaxed = true)
    private val refreshToken =
        RefreshToken(authService, tokenRepository, authCredentialProvider, TokenResponseMapper())

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()

        // Set defaults
        every { tokenRepository.setUserlessToken(any()) } returns Single.just(0)
        every { tokenRepository.updateUserToken(any()) } returns Completable.complete()
    }

    @Test
    fun `should refresh user token`() {
        //Arrange
        val cachedUserToken = createUserToken()
        every { tokenRepository.getCurrentToken() } returns Maybe.just(cachedUserToken)

        val authCredential = "BASIC_AUTH_CREDENTIAL"
        every { authCredentialProvider.getAuthCredential() } returns authCredential

        val refreshedUserToken = createTokenResponse(accessToken = "REFRESHED_ACCESS_TOKEN")
        every {
            authService.refreshUserToken(
                "refresh_token",
                cachedUserToken.refreshToken,
                authCredential
            )
        } returns Single.just(refreshedUserToken)

        // Act
        refreshToken.execute()
            .test()
            .assertNoErrors()

        // Assert
        val expectedToken = UserToken(
            cachedUserToken.id,
            refreshedUserToken.accessToken,
            refreshedUserToken.type,
            refreshedUserToken.refreshToken!!
        )
        verify { tokenRepository.updateUserToken(withArg { assertThat(it).isEqualTo(expectedToken) }) }
    }

    @Test
    fun `should return the refreshed user token`() {
        //Arrange
        val cachedUserToken = createUserToken()
        every { tokenRepository.getCurrentToken() } returns Maybe.just(cachedUserToken)

        val refreshedUserToken = createTokenResponse(accessToken = "REFRESHED_ACCESS_TOKEN")
        every { authService.refreshUserToken(any(), any(), any()) }
            .returns(Single.just(refreshedUserToken))

        //Act, Assert
        val expectedToken = UserToken(
            cachedUserToken.id,
            refreshedUserToken.accessToken,
            refreshedUserToken.type,
            refreshedUserToken.refreshToken!!
        )
        refreshToken.execute()
            .test()
            .assertValue(match { assertThat(it).isEqualTo(expectedToken) })
    }

    @Test
    fun `should refresh userless token`() {
        //Arrange
        val cachedUserlessToken = createUserlessToken()
        every { tokenRepository.getCurrentToken() } returns Maybe.just(cachedUserlessToken)

        val authCredential = "BASIC_AUTH_CREDENTIAL"
        every { authCredentialProvider.getAuthCredential() } returns authCredential

        val refreshedUserLessToken = createTokenResponse(accessToken = "REFRESHED_ACCESS_TOKEN")
        every {
            authService.getUserlessToken(
                "https://oauth.reddit.com/grants/installed_client",
                cachedUserlessToken.deviceId,
                authCredential
            )
        } returns Single.just(refreshedUserLessToken)

        //Act
        refreshToken.execute()
            .test()
            .assertNoErrors()

        //Assert
        val expectedToken = UserlessToken(
            cachedUserlessToken.id,
            refreshedUserLessToken.accessToken,
            refreshedUserLessToken.type,
            cachedUserlessToken.deviceId
        )
        verify { tokenRepository.setUserlessToken(withArg { assertThat(it).isEqualTo(expectedToken) }) }
    }

    @Test
    fun `should return the refreshed userless token`() {
        //Arrange
        val cachedUserlessToken = createUserlessToken()
        every { tokenRepository.getCurrentToken() } returns Maybe.just(cachedUserlessToken)

        val refreshedUserLessToken = createTokenResponse(accessToken = "REFRESHED_ACCESS_TOKEN")
        every { authService.getUserlessToken(any(), any(), any()) }
            .returns(Single.just(refreshedUserLessToken))

        //Act, Assert
        val expectedToken = UserlessToken(
            cachedUserlessToken.id,
            refreshedUserLessToken.accessToken,
            refreshedUserLessToken.type,
            cachedUserlessToken.deviceId
        )
        refreshToken.execute()
            .test()
            .assertValue(match { assertThat(it).isEqualTo(expectedToken) })
    }

    @Test
    fun `when there is no current token should return error`() {
        //Arrange
        every { tokenRepository.getCurrentToken() } returns Maybe.empty()

        //Act, Assert
        refreshToken.execute()
            .test()
            .assertError(Exception::class.java)
    }
}

