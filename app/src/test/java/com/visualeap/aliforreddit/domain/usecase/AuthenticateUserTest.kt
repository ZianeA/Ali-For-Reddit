package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.domain.repository.AccountRepository
import com.visualeap.aliforreddit.domain.repository.RedditorRepository
import com.visualeap.aliforreddit.domain.repository.TokenRepository
import io.mockk.*
import io.mockk.junit5.MockKExtension
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.HttpUrl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import util.domain.*
import java.io.IOException
import java.net.MalformedURLException
import java.sql.SQLException
import kotlin.IllegalArgumentException

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
class AuthenticateUserTest {

    companion object {
        private const val STATE = "STATE"
    }

    private val tokenRepository: TokenRepository = mockk()
    private val accountRepository: AccountRepository = mockk()
    private val redditorRepository: RedditorRepository = mockk()
    private val authenticateUser =
        AuthenticateUser(tokenRepository, accountRepository, redditorRepository)

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
    }

    @Test
    fun `fetch user token`() {
        //Arrange
        every { tokenRepository.getUserToken(CODE) } returns Single.just(createUserToken())
        every { tokenRepository.setCurrentToken(any()) } returns Completable.complete()
        every { redditorRepository.getCurrentRedditor() } returns Single.just(createRedditor())
        every { accountRepository.addAccount(any()) } returns Completable.complete()

        //Act, Assert
        authenticateUser.execute(createParams())
            .test()
            .assertResult()
    }

    @Test
    fun `set the fetched user token as the current token`() {
        //Arrange
        val token = createUserToken()
        every { tokenRepository.getUserToken(CODE) } returns Single.just(token)
        every { tokenRepository.setCurrentToken(any()) } returns Completable.complete()

        //Act, Assert
        authenticateUser.execute(createParams())
            .test()

        verify { tokenRepository.setCurrentToken(token) }
    }

    @Test
    fun `return error when setting current token fails`() {
        //Arrange
        val token = createUserToken()
        every { tokenRepository.getUserToken(CODE) } returns Single.just(token)
        every { tokenRepository.setCurrentToken(any()) } returns Completable.error(SQLException())
        every { redditorRepository.getCurrentRedditor() } returns Single.just(createRedditor())
        every { accountRepository.addAccount(any()) } returns Completable.complete()

        //Act, Assert
        authenticateUser.execute(createParams())
            .test()
            .assertFailure(SQLException::class.java)
    }

    @Test
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
    }

    //TODO handle authentication of an existing user

    @Test
    fun `return error when the final redirect url is malformed`() {
        //Arrange
        val finalUrl = "Malformed URL"
        val params = createParams(finalUrl)

        //Act, Assert
        authenticateUser.execute(params)
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

        val params = createParams(finalUrl)

        //Act, Assert
        authenticateUser.execute(params)
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

        val params = createParams(finalUrl)

        //Act, Assert
        authenticateUser.execute(params)
            .test()
            .assertFailure(IllegalArgumentException::class.java)
    }

    @Test
    fun `return error when final redirect url doesn't contain state`() {
        //Arrange
        val finalUrl = HttpUrl.parse(REDIRECT_URL)!!
            .newBuilder()
            .addQueryParameter("code", CODE)
            .build()
            .toString()

        val params = createParams(finalUrl)

        //Act, Assert
        authenticateUser.execute(params)
            .test()
            .assertFailure(IllegalArgumentException::class.java)
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

        val params = createParams(finalUrl)

        //Act, Assert
        authenticateUser.execute(params)
            .test()
            .assertFailure(IllegalStateException::class.java)
    }

    private fun createParams(finalUrl: String = createCorrectFinalUrl()) =
        AuthenticateUser.Params(finalUrl, STATE)

    private fun createCorrectFinalUrl(): String {
        return HttpUrl.parse(REDIRECT_URL)!!
            .newBuilder()
            .addQueryParameter("code", CODE)
            .addQueryParameter("state", STATE)
            .build()
            .toString()
    }
}