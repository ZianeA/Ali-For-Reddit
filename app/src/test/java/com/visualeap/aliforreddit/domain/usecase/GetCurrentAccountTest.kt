/*
package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.domain.repository.AccountRepository
import com.visualeap.aliforreddit.util.createAccount
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Observable
import io.reactivex.Single
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import io.reactivex.plugins.RxJavaPlugins
import java.sql.SQLException


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class GetCurrentAccountTest {
    private val accountRepository: AccountRepository = mockk()
    private val getCurrentAccount = GetCurrentAccount(accountRepository)

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
    }

    @Test
    fun `return logged-in account when there is only one account`() {
        //Arrange
        val loggedInAccount = createAccount(isLoggedIn = true)
        every { accountRepository.getAccounts() } returns Single.just(listOf(loggedInAccount))

        //Act
        val account = getCurrentAccount.execute(Unit)

        //Assert
        assertThat(account).isEqualTo(loggedInAccount)
    }

    @Test
    fun `return logged-in account when there are multiple accounts`() {
        //Arrange
        val loggedInAccount = createAccount(isLoggedIn = true)
        val accounts = listOf(
            createAccount(isLoggedIn = false),
            loggedInAccount,
            createAccount(isLoggedIn = false)
        )

        every { accountRepository.getAccounts() } returns Single.just(accounts)

        //Act
        val account = getCurrentAccount.execute(Unit)

        //Assert
        assertThat(account).isEqualTo(loggedInAccount)
    }

    @Test
    fun `throw exception when more that one account is logged-in`() {
        //Arrange
        val accounts = listOf(createAccount(isLoggedIn = true), createAccount(isLoggedIn = true))
        every { accountRepository.getAccounts() } returns Single.just(accounts)


        //Act, Assert
        assertThatIllegalStateException().isThrownBy { getCurrentAccount.execute(Unit) }
    }

    @Test
    fun `throw exception when database fails`() {
        //Arrange
        val accounts = listOf(createAccount(isLoggedIn = true), createAccount(isLoggedIn = true))
        every { accountRepository.getAccounts() } returns Single.error(SQLException())


        //Act, Assert
        assertThatThrownBy { getCurrentAccount.execute(Unit) }.isInstanceOf(SQLException::class.java)
    }

    @Test
    fun `return null when there are no accounts`() {
        //Arrange
        every { accountRepository.getAccounts() } returns Single.just(emptyList())

        //Act
        val account = getCurrentAccount.execute(Unit)

        //Assert
        assertThat(account).isNull()
    }
}*/
