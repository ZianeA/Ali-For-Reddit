package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.SyncSchedulerProvider
import com.visualeap.aliforreddit.domain.model.Account
import com.visualeap.aliforreddit.domain.repository.AccountRepository
import com.visualeap.aliforreddit.domain.repository.RedditorRepository
import com.visualeap.aliforreddit.domain.repository.TokenRepository
import com.visualeap.aliforreddit.util.*
import io.mockk.*
import io.mockk.junit5.MockKExtension
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.HttpUrl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import java.io.IOException
import java.net.MalformedURLException
import java.sql.SQLException
import kotlin.IllegalArgumentException

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
class AuthenticateUserTest {

    companion object {
        private const val REDIRECT_URL = "https://example.com/path"
        private const val CODE = "CODE"
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
        every { redditorRepository.getCurrentRedditor() } returns Single.just(createRedditor())
        every { accountRepository.saveAccount(any()) } returns Completable.complete()

        //Act, Assert
        authenticateUser.execute(createParams())
            .test()
            .assertResult()
    }

    @Test
    fun `create a new account`() {
        //Arrange
        val token = createUserToken(id = 101)
        val redditor = createRedditor("Special User")
        val account = createAccount(id = 0, redditor = redditor, token = token)

        every { tokenRepository.getUserToken(any()) } returns Single.just(token)
        every { redditorRepository.getCurrentRedditor() } returns Single.just(redditor)
        every { accountRepository.saveAccount(any()) } returns Completable.complete()

        //Act, Assert
        authenticateUser.execute(createParams())
            .test()
            .assertResult()

        verify { accountRepository.saveAccount(account) }
    }

    @Test
    fun `return error when creating account fails`() {
        //Arrange
        every { tokenRepository.getUserToken(any()) } returns Single.just(createUserToken())
        every { redditorRepository.getCurrentRedditor() } returns Single.just(createRedditor())
        every { accountRepository.saveAccount(any()) } returns Completable.error(SQLException())

        //Act, assert
        authenticateUser.execute(createParams())
            .test()
            .assertFailure(SQLException::class.java)
    }

    @Test
    fun `return error when getting current redditor fails`() {
        //Arrange
        every { tokenRepository.getUserToken(any()) } returns Single.just(createUserToken())
        every { redditorRepository.getCurrentRedditor() } returns Single.error(IOException())
        every { accountRepository.saveAccount(any()) } returns Completable.complete()

        //Act, assert
        authenticateUser.execute(createParams())
            .test()
            .assertFailure(IOException::class.java)
    }

    //TODO to be deleted
    /*@Test
    fun `switch current account to the authenticated user account`() {
        //Arrange
        every {
            tokenRepository.getUserToken(
                GRANT_TYPE,
                CODE,
                REDIRECT_URL,
                basicAuth
            )
        } returns Single.just(
            createUserToken()
        )

        every { accountRepository.saveAccount(any()) } returns Completable.complete()

        //Act, assert
        authenticateUser.execute(createParams(createCorrectFinalUrl()))
            .test()
    }*/

    /*@Test
    fun `return error when switching accounts fails`() {
        //Arrange
        every { tokenRepository.getUserToken(any(), any(), any(), any()) } returns Single.just(
            createUserToken()
        )
        every { accountRepository.saveAccount(any()) } returns Completable.complete()

        //Act, Assert
        authenticateUser.execute(createParams(createCorrectFinalUrl()))
            .test()
            .assertFailure(Throwable::class.java)
    }*/

    /*@Test
    fun `update account with username and avatar url`() {
        //Arrange
        val user = createUser()
        val updatedAccount = createAccount()
        val params = createParams(createCorrectFinalUrl())
        every { tokenRepository.getUserToken(any(), any(), any(), any()) } returns Single.just(
            createUserToken()
        )
        every { accountRepository.saveAccount(any()) } returns Completable.complete()
        every { accountRepository.updateAccount(any()) } returns Completable.complete()

        //Act, assert
        authenticateUser.execute(params)
            .test()
            .assertResult()

        verify { accountRepository.updateAccount(updatedAccount) }
    }

    @Test
    fun `return error when adding username and avatar url to saved account fails`() {
        //Arrange
        val params = createParams(createCorrectFinalUrl())
        every { tokenRepository.getUserToken(any(), any(), any(), any()) } returns Single.just(
            createUserToken()
        )
        every { accountRepository.saveAccount(any()) } returns Completable.complete()
        every { accountRepository.updateAccount(any()) } returns Completable.error(SQLException())

        //Act, assert
        authenticateUser.execute(params)
            .test()
            .assertFailure(SQLException::class.java)
    }*/

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