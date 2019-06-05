package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.SyncSchedulerProvider
import com.visualeap.aliforreddit.data.network.RedditService
import com.visualeap.aliforreddit.domain.entity.Account
import com.visualeap.aliforreddit.domain.repository.AccountRepository
import com.visualeap.aliforreddit.util.createAccount
import com.visualeap.aliforreddit.util.createBasicAuth
import com.visualeap.aliforreddit.util.createUser
import com.visualeap.aliforreddit.util.createUserToken
import io.mockk.*
import io.mockk.junit5.MockKExtension
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.HttpUrl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
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
        private const val GRANT_TYPE = "authorization_code"
    }

    private val authService: AuthService = mockk()
    private val redditService: RedditService = mockk()
    private val switchLoginAccount: SwitchLoginAccount = mockk()
    private val basicAuth = createBasicAuth()
    private val accountRepository: AccountRepository = mockk()
    private val authenticateUser =
        AuthenticateUser(
            SyncSchedulerProvider(),
            authService,
            redditService,
            accountRepository,
            switchLoginAccount,
            REDIRECT_URL,
            basicAuth
        )

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
    }

    @Test
    fun `save the user-token inside account`() {
        //Arrange
        val params = createParams(createCorrectFinalUrl())
        val token = createUserToken()
        val account = Account(
            Account.UNKNOWN_ACCOUNT_USERNAME,
            token,
            false,
            null
        )

        every {
            authService.getUserToken(
                GRANT_TYPE,
                CODE,
                REDIRECT_URL,
                basicAuth
            )
        } returns Single.just(token)

        every { accountRepository.saveAccount(any()) } returns Completable.complete()
        every { redditService.getCurrentUser() } returns Single.just(createUser())
        every { accountRepository.updateAccount(any()) } returns Completable.complete()
        every { switchLoginAccount.execute(any()) } returns Completable.complete()

        //Act, Assert
        authenticateUser.execute(params)
            .test()
            .assertResult()

        verify { accountRepository.saveAccount(account) }
    }

    @Test
    fun `return error when saving account fails`() {
        //Arrange
        val params = createParams(createCorrectFinalUrl())
        every { authService.getUserToken(any(), any(), any(), any()) } returns Single.just(
            createUserToken()
        )
        every { accountRepository.saveAccount(any()) } returns Completable.error(SQLException())
        every { redditService.getCurrentUser() } returns Single.just(createUser())
        every { accountRepository.updateAccount(any()) } returns Completable.complete()
        every { switchLoginAccount.execute(any()) } returns Completable.complete()

        //Act, assert
        authenticateUser.execute(params)
            .test()
            .assertFailure(SQLException::class.java)
    }

    @Test
    fun `switch current account to the authenticated user account`() {
        //Arrange
        every {
            authService.getUserToken(
                GRANT_TYPE,
                CODE,
                REDIRECT_URL,
                basicAuth
            )
        } returns Single.just(
            createUserToken()
        )

        every { accountRepository.saveAccount(any()) } returns Completable.complete()
        every { switchLoginAccount.execute(any()) } returns Completable.complete()

        //Act, assert
        authenticateUser.execute(createParams(createCorrectFinalUrl()))
            .test()

        verify { switchLoginAccount.execute(Account.UNKNOWN_ACCOUNT_USERNAME) }
    }

    @Test
    fun `return error when switching accounts fails`() {
        //Arrange
        every { authService.getUserToken(any(), any(), any(), any()) } returns Single.just(
            createUserToken()
        )
        every { accountRepository.saveAccount(any()) } returns Completable.complete()
        every { switchLoginAccount.execute(any()) } returns Completable.error(Throwable())

        //Act, Assert
        authenticateUser.execute(createParams(createCorrectFinalUrl()))
            .test()
            .assertFailure(Throwable::class.java)
    }

    @Test
    fun `update account with username and avatar url`() {
        //Arrange
        val user = createUser()
        val updatedAccount = createAccount(username = user.name, avatarUrl = user.avatarUrl)
        val params = createParams(createCorrectFinalUrl())
        every { authService.getUserToken(any(), any(), any(), any()) } returns Single.just(
            createUserToken()
        )
        every { accountRepository.saveAccount(any()) } returns Completable.complete()
        every { switchLoginAccount.execute(any()) } returns Completable.complete()
        every { redditService.getCurrentUser() } returns Single.just(user)
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
        every { authService.getUserToken(any(), any(), any(), any()) } returns Single.just(
            createUserToken()
        )
        every { accountRepository.saveAccount(any()) } returns Completable.complete()
        every { redditService.getCurrentUser() } returns Single.just(createUser())
        every { accountRepository.updateAccount(any()) } returns Completable.error(SQLException())
        every { switchLoginAccount.execute(any()) } returns Completable.complete()

        //Act, assert
        authenticateUser.execute(params)
            .test()
            .assertFailure(SQLException::class.java)
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

    private fun createParams(finalUrl: String) = AuthenticateUser.Params(finalUrl, STATE)

    private fun createCorrectFinalUrl(): String {
        return HttpUrl.parse(REDIRECT_URL)!!
            .newBuilder()
            .addQueryParameter("code", CODE)
            .addQueryParameter("state", STATE)
            .build()
            .toString()
    }
}