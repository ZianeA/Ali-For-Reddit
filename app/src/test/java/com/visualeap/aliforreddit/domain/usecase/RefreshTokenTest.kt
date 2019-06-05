package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.domain.repository.AccountRepository
import com.visualeap.aliforreddit.util.createAccount
import com.visualeap.aliforreddit.util.createAnonymousAccount
import com.visualeap.aliforreddit.util.createUserToken
import com.visualeap.aliforreddit.util.createUserlessToken
import io.mockk.*
import io.reactivex.Completable
import io.reactivex.Single
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.sql.SQLException

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class RefreshTokenTest {
    private val authService: AuthService = mockk()
    private val getCurrentAccount: GetCurrentAccount = mockk()
    private val getUserLessToken: GetUserLessToken = mockk()
    private val accountRepository: AccountRepository = mockk(relaxUnitFun = true)
    private val refreshToken =
        RefreshToken(authService, getCurrentAccount, getUserLessToken, accountRepository)

    companion object {
        private const val GRANT_TYPE = "refresh_token"
        private const val REFRESHED_ACCESS_TOKEN = "ACCESS TOKEN HAS BEEN REFRESHED"
    }

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
    }

    @Test
    fun `refresh and return user token when user is logged in`() {
        //Arrange
        val storedUserToken = createUserToken()
        val refreshedUserToken =
            createUserToken(accessToken = REFRESHED_ACCESS_TOKEN, refreshToken = null)

        every { getCurrentAccount.execute(Unit) } returns createAccount(token = storedUserToken)
        every {
            authService.refreshUserToken(
                GRANT_TYPE,
                storedUserToken.refreshToken!!
            )
        } returns Single.just(refreshedUserToken)
        every { accountRepository.saveAccount(any()) } returns Completable.complete()

        //Act
        val token = refreshToken.execute(Unit)

        //Assert
        assertThat(token).isEqualTo(refreshedUserToken)
    }

    @Test
    fun `refresh and return user-less token when no user is logged in`() {
        //Arrange
        val storedUserlessToken = createUserlessToken()
        val refreshedUserLessToken =
            createUserlessToken(accessToken = REFRESHED_ACCESS_TOKEN, deviceId = null)

        every { getCurrentAccount.execute(Unit) } returns createAnonymousAccount(storedUserlessToken)
        every { getUserLessToken.execute(storedUserlessToken.deviceId!!) } returns refreshedUserLessToken
        every { accountRepository.saveAccount(any()) } returns Completable.complete()

        //Act
        val token = refreshToken.execute(Unit)

        //Assert
        assertThat(token).isEqualTo(refreshedUserLessToken)
    }

    @Test
    fun `save user token`() {
        //Arrange
        val refreshedUserToken =
            createUserToken(accessToken = REFRESHED_ACCESS_TOKEN, refreshToken = null)

        every { getCurrentAccount.execute(Unit) } returns createAccount()
        every { authService.refreshUserToken(any(), any()) } returns Single.just(refreshedUserToken)
        every { accountRepository.saveAccount(any()) } returns Completable.complete()

        //Act
        refreshToken.execute(Unit)

        //Assert
        val updatedAccount =
            createAccount(token = createUserToken(accessToken = refreshedUserToken.accessToken))
        verify { accountRepository.saveAccount(updatedAccount) }
    }

    @Test
    fun `save user-less token`() {
        //Arrange
        val storedUserlessToken = createUserlessToken()
        val refreshedUserlessToken =
            createUserlessToken(accessToken = REFRESHED_ACCESS_TOKEN, deviceId = null)

        every { getCurrentAccount.execute(Unit) } returns createAnonymousAccount(token = storedUserlessToken)
        every { getUserLessToken.execute(storedUserlessToken.deviceId!!) } returns refreshedUserlessToken
        every { accountRepository.saveAccount(any()) } returns Completable.complete()

        //Act
        refreshToken.execute(Unit)

        //Assert
        val updateAccount =
            createAnonymousAccount(createUserlessToken(accessToken = refreshedUserlessToken.accessToken))
        verify { accountRepository.saveAccount(updateAccount) }
    }

    @Test
    fun `throw exception when saving user token fails`() {
        //Arrange
        every { getCurrentAccount.execute(any()) } returns createAccount()
        every { authService.refreshUserToken(any(), any()) } returns Single.just(
            createUserToken()
        )
        every { accountRepository.saveAccount(any()) } returns Completable.error(SQLException())

        //Act, assert
        assertThatThrownBy { refreshToken.execute(Unit) }.isInstanceOf(SQLException::class.java)
    }

    @Test
    fun `throw exception when saving user-less token fails`() {
        //Arrange
        every { getCurrentAccount.execute(any()) } returns createAnonymousAccount()
        every { getUserLessToken.execute(any()) } returns createUserlessToken()
        every { accountRepository.saveAccount(any()) } returns Completable.error(SQLException())

        //Act, assert
        assertThatThrownBy { refreshToken.execute(Unit) }.isInstanceOf(SQLException::class.java)
    }

    @Test
    fun `not throw exception when user token retrieval fails`() {
        //Arrange
        every { getCurrentAccount.execute(Unit) } returns createAccount()
        every { authService.refreshUserToken(any(), any()) } returns Single.error(Throwable())

        //Act, Assert
        assertThatCode { refreshToken.execute(Unit) }.doesNotThrowAnyException()
    }

    @Test
    fun `not throw exception when user-less token retrieval fails`() {
        //Arrange
        every { getCurrentAccount.execute(Unit) } returns createAnonymousAccount()
        every { getUserLessToken.execute(any()) } returns null

        //Act, Assert
        assertThatCode { refreshToken.execute(Unit) }.doesNotThrowAnyException()
    }

    @Test
    fun `throw exception when refresh token is null`() {
        //Arrange
        val storedUserToken = createUserToken(refreshToken = null)
        every { getCurrentAccount.execute(Unit) } returns createAccount(token = storedUserToken)

        //Act, Assert
        assertThatIllegalStateException().isThrownBy { refreshToken.execute(Unit) }
    }

    @Test
    fun `throw exception when refresh token is empty`() {
        //Arrange
        val storedUserToken = createUserToken(refreshToken = "")
        every { getCurrentAccount.execute(Unit) } returns createAccount(token = storedUserToken)

        //Act, Assert
        assertThatIllegalStateException().isThrownBy { refreshToken.execute(Unit) }
    }

    @Test
    fun `throw exception when device id is null`() {
        //Arrange
        val storedUserlessToken = createUserlessToken(deviceId = null)
        every { getCurrentAccount.execute(Unit) } returns createAccount(token = storedUserlessToken)

        //Act, Assert
        assertThatIllegalStateException().isThrownBy { refreshToken.execute(Unit) }
    }

    @Test
    fun `throw exception when device id is empty`() {
        //Arrange
        val storedUserlessToken = createUserlessToken(deviceId = "")
        every { getCurrentAccount.execute(Unit) } returns createAccount(token = storedUserlessToken)

        //Act, Assert
        assertThatIllegalStateException().isThrownBy { refreshToken.execute(Unit) }
    }
}

