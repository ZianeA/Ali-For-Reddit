package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.domain.repository.TokenRepository
import com.visualeap.aliforreddit.util.createAccount
import com.visualeap.aliforreddit.util.createUserToken
import com.visualeap.aliforreddit.util.createUserlessToken
import io.mockk.*
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.sql.SQLException

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
            tokenRepository.refreshUserToken(currentToken.refreshToken)
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
        every { tokenRepository.refreshUserLessToken(currentToken.deviceId) } returns Single.just(
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
            .assertError(IllegalStateException::class.java)
    }

    //TODO this should be the token repository responsibility
    /*@Test
    fun `save user token`() {
        //Arrange
        val refreshedUserToken =
            createUserToken(accessToken = REFRESHED_ACCESS_TOKEN, refreshToken = null)

        every { getCurrentAccount.execute(Unit) } returns createAccount()
        every { tokenRepository.refreshUserToken(any(), any()) } returns Single.just(
            refreshedUserToken
        )
        every { accountRepository.saveAccount(any()) } returns Completable.complete()

        //Act
        refreshToken.execute(Unit)

        //Assert
        val updatedAccount =
            createAccount(token = createUserToken(accessToken = refreshedUserToken.accessToken))
        verify { accountRepository.saveAccount(updatedAccount) }
    }*/

    /*@Test
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
    }*/

    /*@Test
    fun `throw exception when saving user token fails`() {
        //Arrange
        every { getCurrentAccount.execute(any()) } returns createAccount()
        every { tokenRepository.refreshUserToken(any(), any()) } returns Single.just(
            createUserToken()
        )
        every { accountRepository.saveAccount(any()) } returns Completable.error(SQLException())

        //Act, assert
        assertThatThrownBy { refreshToken.execute(Unit) }.isInstanceOf(SQLException::class.java)
    }*/

    /*@Test
    fun `throw exception when saving user-less token fails`() {
        //Arrange
        every { getCurrentAccount.execute(any()) } returns createAnonymousAccount()
        every { getUserLessToken.execute(any()) } returns createUserlessToken()
        every { accountRepository.saveAccount(any()) } returns Completable.error(SQLException())

        //Act, assert
        assertThatThrownBy { refreshToken.execute(Unit) }.isInstanceOf(SQLException::class.java)
    }*/

    //TODO remove these as they are deprecated
/*    @Test
    fun `not throw exception when user-token retrieval fails`() {
        //Arrange
        every { getCurrentAccount.execute(Unit) } returns createAccount()
//        every { tokenRepository.refreshUserToken(any(), any()) } returns Single.error(Throwable())

        //Act, Assert
        assertThatCode { refreshToken.execute(Unit) }.doesNotThrowAnyException()
    }*/

/*    @Test
    fun `not throw exception when user-less token retrieval fails`() {
        //Arrange
//        every { getCurrentAccount.execute(Unit) } returns createAnonymousAccount()
        every { getUserLessToken.execute(any()) } returns null

        //Act, Assert
        assertThatCode { refreshToken.execute(Unit) }.doesNotThrowAnyException()
    }*/

/*    @Test
    fun `throw exception when refresh token is null`() {
        //Arrange
        val storedUserToken = createUserToken(refreshToken = null)
        every { getCurrentAccount.execute(Unit) } returns createAccount(token = storedUserToken)

        //Act, Assert
        assertThatIllegalStateException().isThrownBy { refreshToken.execute(Unit) }
    }*/

/*    @Test
    fun `throw exception when refresh token is empty`() {
        //Arrange
        val storedUserToken = createUserToken(refreshToken = "")
        every { getCurrentAccount.execute(Unit) } returns createAccount(token = storedUserToken)

        //Act, Assert
        assertThatIllegalStateException().isThrownBy { refreshToken.execute(Unit) }
    }*/

/*    @Test
    fun `throw exception when device id is null`() {
        //Arrange
        val storedUserlessToken = createUserlessToken(deviceId = null)
        every { getCurrentAccount.execute(Unit) } returns createAccount(token = storedUserlessToken)

        //Act, Assert
        assertThatIllegalStateException().isThrownBy { refreshToken.execute(Unit) }
    }*/

/*    @Test
    fun `throw exception when device id is empty`() {
        //Arrange
        val storedUserlessToken = createUserlessToken(deviceId = "")
        every { getCurrentAccount.execute(Unit) } returns createAccount(token = storedUserlessToken)

        //Act, Assert
        assertThatIllegalStateException().isThrownBy { refreshToken.execute(Unit) }
    }*/
}

