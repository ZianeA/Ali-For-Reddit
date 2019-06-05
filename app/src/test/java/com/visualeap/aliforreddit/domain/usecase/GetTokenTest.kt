package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.domain.entity.token.Token
import com.visualeap.aliforreddit.domain.repository.AccountRepository
import com.visualeap.aliforreddit.util.*
import io.mockk.*
import io.reactivex.Completable
import io.reactivex.Single
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.sql.SQLException

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GetTokenTest {
    private val getCurrentAccount: GetCurrentAccount = mockk()
    private val accountRepository: AccountRepository = mockk(relaxUnitFun = true)
    private val getUserLessToken: GetUserLessToken = mockk()
    private val getToken = GetToken(getCurrentAccount, accountRepository, getUserLessToken)

    companion object {
        private const val ANONYMOUS_ACCOUNT_USERNAME = "Anonymous"
    }

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
    }

    @Test
    fun `return current user token when user is logged in`() {
        //Arrange
        val expectedToken = createUserToken()
        val account = createAccount(token = expectedToken)
        every { getCurrentAccount.execute(Unit) } returns account

        //Act
        val token = getToken.execute(Unit)

        //Assert
        assertThat(token).isEqualTo(expectedToken)
    }

    @Test
    fun `return user-less token when no user is logged in`() {
        //Arrange
        val expectedToken = createUserlessToken()
        every { getCurrentAccount.execute(Unit) } returns null
        every { getUserLessToken.execute(any()) } returns expectedToken
        every { accountRepository.saveAccount(any()) } returns Completable.complete()

        //Act
        val token = getToken.execute(Unit)

        //Assert
        assertThat(token).isEqualToIgnoringGivenFields(expectedToken, "deviceId")
    }

    @Test
    fun `store user-less token after retrieval`() {
        //Arrange
        val expectedToken = createUserlessToken()
        val expectedAccount = createAnonymousAccount(expectedToken)

        every { getCurrentAccount.execute(Unit) }.returnsMany(null, expectedAccount)
        every { getUserLessToken.execute(any()) } returns expectedToken
        every { accountRepository.saveAccount(any()) } returns Completable.complete()

        //Act
        var token: Token? = null

        repeat(3) {
            token = getToken.execute(Unit)
        }

        //Assert
        //Fetch the user-less token from the server only once
        verify(atMost = 1) { getUserLessToken.execute(any()) }

        //Save the user-less token only once, and Create the correct Anonymous account
        verify(atMost = 1) {
            accountRepository.saveAccount(
                withArg {
                    assertThat(it).isEqualToIgnoringGivenFields(expectedAccount, "deviceId")
                })
        }

        //Return the correct token
        assertThat(token).isEqualToIgnoringGivenFields(expectedToken, "deviceId")
    }

    @Test
    fun `throw an exception when saving token fails`() {
        //Arrange
        every { getCurrentAccount.execute(Unit) } returns null
        every { getUserLessToken.execute(any()) } returns createUserlessToken()
        every { accountRepository.saveAccount(any()) } returns Completable.error(SQLException())

        //Act, Assert
        assertThatThrownBy { getToken.execute(Unit) }.isInstanceOf(SQLException::class.java)
    }

    @Test
    fun `not throw an exception when token retrieval fails`() {
        //Arrange
        every { getCurrentAccount.execute(Unit) } returns null
        every { getUserLessToken.execute(any()) } returns null

        //Act, Assert
        assertThatCode { getToken.execute(Unit) }.doesNotThrowAnyException()
    }
}