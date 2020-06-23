package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.data.token.AuthService
import com.visualeap.aliforreddit.domain.authentication.AuthenticateUser
import com.visualeap.aliforreddit.domain.util.BasicAuthCredentialProvider
import com.visualeap.aliforreddit.domain.authentication.token.UserToken
import com.visualeap.aliforreddit.domain.authentication.TokenRepository
import com.visualeap.aliforreddit.domain.util.OAuthException
import com.visualeap.aliforreddit.domain.util.TokenResponseMapper
import io.mockk.*
import io.mockk.junit5.MockKExtension
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.HttpUrl
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import util.domain.*
import java.net.MalformedURLException

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
class AuthenticateUserTest {
    companion object {
        private const val STATE = "STATE"
        private const val AUTH_URL = "https://www.reddit.com/api/v1/authorize?state=$STATE"
        private const val CODE = "CODE"
        private const val REDIRECT_URL = "https://example.com/path"
    }

    private val authService: AuthService = mockk()
    private val tokenRepository: TokenRepository = mockk()
    private val authCredentialProvider: BasicAuthCredentialProvider = mockk(relaxed = true)
    private val authenticateUser =
        AuthenticateUser(
            authService,
            tokenRepository,
            authCredentialProvider,
            TokenResponseMapper()
        )

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()

        // Set defaults
        every { tokenRepository.addUserToken(any()) } returns Single.just(0)
        every { tokenRepository.setCurrentToken(any()) } returns Completable.complete()
    }

    @Test
    fun `should save fetched token`() {
        //Arrange
        val basicAuth = "BASIC_AUTH_CREDENTIAL"
        every { authCredentialProvider.getAuthCredential() } returns basicAuth
        val grantType = "authorization_code"
        val token = createTokenResponse()
        every {
            authService.getUserToken(grantType, CODE, REDIRECT_URL, basicAuth)
        } returns Single.just(token)

        //Act
        authenticateUser.execute(REDIRECT_URL, buildValidFinalUrl(), AUTH_URL)
            .test()
            .assertResult()

        // Assert
        verify {
            tokenRepository.addUserToken(withArg {
                assertThat(it)
                    .isEqualTo(UserToken(0, token.accessToken, token.type, token.refreshToken!!))
            })
        }
    }

    //TODO handle authentication of an existing user

    @Test
    fun `should set the fetched token as the current token`() {
        //Arrange
        val token = createTokenResponse()
        every { tokenRepository.addUserToken(any()) } returns Single.just(202)
        every { authService.getUserToken(any(), any(), any(), any()) } returns Single.just(token)

        //Act, Assert
        authenticateUser.execute(REDIRECT_URL, buildValidFinalUrl(), AUTH_URL)
            .test()
            .assertResult()

        verify {
            tokenRepository.setCurrentToken(withArg {
                assertThat(it)
                    .isEqualTo(UserToken(202, token.accessToken, token.type, token.refreshToken!!))
            })
        }
    }

    /* @Test
    fun `create a new account`() {
        //Arrange
        val token = createUserToken()
        val redditor = createRedditor()

        every { tokenRepository.getUserToken(any()) } returns Single.just(token)
        every { tokenRepository.setCurrentToken(any()) } returns Completable.complete()
        every { redditorRepository.getCurrentRedditor() } returns Single.just(redditor)
        every { accountRepository.addAccount(any()) } returns Completable.complete()

        //Act, Assert
        authenticateUser.execute(createParams())
            .test()

        verify { accountRepository.addAccount(createAccount()) }
    }

    @Test
    fun `return error when creating account fails`() {
        //Arrange
        every { tokenRepository.getUserToken(any()) } returns Single.just(createUserToken())
        every { tokenRepository.setCurrentToken(any()) } returns Completable.complete()
        every { redditorRepository.getCurrentRedditor() } returns Single.just(createRedditor())
        every { accountRepository.addAccount(any()) } returns Completable.error(SQLException())

        //Act, assert
        authenticateUser.execute(createParams())
            .test()
            .assertFailure(SQLException::class.java)
    }

    @Test
    fun `return error when getting current redditor fails`() {
        //Arrange
        every { tokenRepository.getUserToken(any()) } returns Single.just(createUserToken())
        every { tokenRepository.setCurrentToken(any()) } returns Completable.complete()
        every { redditorRepository.getCurrentRedditor() } returns Single.error(IOException())
        every { accountRepository.addAccount(any()) } returns Completable.complete()

        //Act, assert
        authenticateUser.execute(createParams())
            .test()
            .assertFailure(IOException::class.java)
    }*/

    @Test
    fun `return error when the final redirect url is malformed`() {
        //Arrange
        val finalUrl = "Malformed URL"

        //Act, Assert
        authenticateUser.execute(REDIRECT_URL, finalUrl, AUTH_URL)
            .test()
            .assertFailure(MalformedURLException::class.java)
    }

    @Test
    fun `return error when final redirect url contains error query`() {
        //Arrange
        val finalUrl = HttpUrl.parse(REDIRECT_URL)!!
            .newBuilder()
            .addQueryParameter("error", "ERROR")
            .build()
            .toString()

        //Act, Assert
        authenticateUser.execute(REDIRECT_URL, finalUrl, AUTH_URL)
            .test()
            .assertFailure(OAuthException::class.java)
    }

    @Test
    fun `return error when final redirect url doesn't contain code`() {
        //Arrange
        val finalUrl = HttpUrl.parse(REDIRECT_URL)!!
            .newBuilder()
            .addQueryParameter("code", CODE)
            .build()
            .toString()

        //Act, Assert
        authenticateUser.execute(REDIRECT_URL, finalUrl, AUTH_URL)
            .test()
            .assertFailure(NullPointerException::class.java)
    }

    @Test
    fun `return error when final redirect url doesn't contain state`() {
        //Arrange
        val finalUrl = HttpUrl.parse(REDIRECT_URL)!!
            .newBuilder()
            .addQueryParameter("code", CODE)
            .build()
            .toString()

        //Act, Assert
        authenticateUser.execute(REDIRECT_URL, finalUrl, AUTH_URL)
            .test()
            .assertFailure(NullPointerException::class.java)
    }

    @Test
    fun `return error when state doesn't match`() {
        //Arrange
        val finalUrl = HttpUrl.parse(REDIRECT_URL)!!
            .newBuilder()
            .addQueryParameter("code", CODE)
            .addQueryParameter("state", "Invalid state")
            .build()
            .toString()

        //Act, Assert
        authenticateUser.execute(REDIRECT_URL, finalUrl, AUTH_URL)
            .test()
            .assertFailure(IllegalStateException::class.java)
    }

    private fun buildValidFinalUrl(): String {
        return HttpUrl.parse(REDIRECT_URL)!!
            .newBuilder()
            .addQueryParameter("code", CODE)
            .addQueryParameter("state", STATE)
            .build()
            .toString()
    }
}