package com.visualeap.aliforreddit.domain.usecase

import com.visualeap.aliforreddit.SyncSchedulerProvider
import com.visualeap.aliforreddit.domain.repository.AccountRepository
import com.visualeap.aliforreddit.util.createAccount
import com.visualeap.aliforreddit.util.createAnonymousAccount
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.sql.SQLException

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class SwitchLoginAccountTest {
    private val accountRepository: AccountRepository = mockk()
    private val switchLoginAccount = SwitchLoginAccount(SyncSchedulerProvider(), accountRepository)

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
    }

    @Test
    fun `sign in the specified account`() {
        //Arrange
        val targetAccount = createAccount(username = "Target account", isLoggedIn = false)
        val accounts = listOf(targetAccount, createAnonymousAccount())
        every { accountRepository.getAccounts() } returns Single.just(accounts)
        every { accountRepository.updateAccount(any()) } returns Completable.complete()

        //Act, Assert
        switchLoginAccount.execute(targetAccount.username)
            .test()
            .assertResult()

        val expectedAccount = targetAccount.copy(isLoggedIn = true)
        verify { accountRepository.updateAccount(expectedAccount) }
    }

    @Test
    fun `sign out the other account when sign-in`() {
        //Arrange
        val targetAccount = createAccount(username = "Target Account", isLoggedIn = false)
        val otherAccount = createAnonymousAccount(isLoggedIn = true)
        every { accountRepository.getAccounts() } returns Single.just(
            listOf(
                targetAccount,
                otherAccount
            )
        )
        every { accountRepository.updateAccount(any()) } returns Completable.complete()

        //Act, Assert
        switchLoginAccount.execute(targetAccount.username)
            .test()
            .assertResult()

        val expectedAccount = otherAccount.copy(isLoggedIn = false)
        verify { accountRepository.updateAccount(expectedAccount) }
    }

    @Test
    fun `sign out all accounts when more than one account is sign-in`() {
        //Arrange
        val targetAccount = createAccount(username = "Target Account", isLoggedIn = false)
        every { accountRepository.getAccounts() } returns Single.just(
            listOf(
                targetAccount,
                createAccount(isLoggedIn = true),
                createAnonymousAccount(isLoggedIn = true)
            )
        )
        every { accountRepository.updateAccount(any()) } returns Completable.complete()

        //Act, assert
        switchLoginAccount.execute(targetAccount.username)
            .test()
            .assertResult()

        verify(atLeast = 2) { accountRepository.updateAccount(match { !it.isLoggedIn }) }
        verify { accountRepository.updateAccount(targetAccount.copy(isLoggedIn = true)) }
    }

    @Test
    fun `return error when there are no accounts`() {
        //Arrange
        every { accountRepository.getAccounts() } returns Single.just(emptyList())

        //Act, Assert
        switchLoginAccount.execute("")
            .test()
            .assertFailure(IllegalStateException::class.java)
    }

    @Test
    fun `return error when there are less than two accounts`() {
        //Arrange
        every { accountRepository.getAccounts() } returns Single.just(listOf(createAnonymousAccount()))

        //Act, Assert
        switchLoginAccount.execute("")
            .test()
            .assertFailure(IllegalStateException::class.java)
    }

    @Test
    fun `return error when there are no accounts with the supplied username`() {
        //Arrange
        every { accountRepository.getAccounts() } returns Single.just(
            listOf(
                createAccount(
                    isLoggedIn = false
                ), createAnonymousAccount()
            )
        )
        every { accountRepository.updateAccount(any()) } returns Completable.complete()

        //Act, assert
        switchLoginAccount.execute("")
            .test()
            .assertFailure(IllegalArgumentException::class.java)
    }

    @Test
    fun `return error when querying accounts fails`() {
        //Arrange
        every { accountRepository.getAccounts() } returns Single.error(SQLException())

        //Act, assert
        switchLoginAccount.execute("")
            .test()
            .assertFailure(SQLException::class.java)
    }

    @Test
    fun `return error when signing out fails`() {
        //Arrange
        val targetAccount = createAccount(username = "Target Account", isLoggedIn = false)
        every { accountRepository.getAccounts() } returns Single.just(
            listOf(
                targetAccount,
                createAnonymousAccount()
            )
        )
        every { accountRepository.updateAccount(match { !it.isLoggedIn }) } returns Completable.error(
            SQLException()
        )
        every { accountRepository.updateAccount(match { it.isLoggedIn }) } returns Completable.complete()

        //Act, assert
        switchLoginAccount.execute(targetAccount.username)
            .test()
            .assertFailure(SQLException::class.java)
    }

    @Test
    fun `return error when signing in fails`() {
        //Arrange
        val targetAccount = createAccount(username = "Target Account", isLoggedIn = false)
        every { accountRepository.getAccounts() } returns Single.just(
            listOf(
                targetAccount,
                createAnonymousAccount()
            )
        )
        every { accountRepository.updateAccount(match { it.isLoggedIn }) } returns Completable.error(
            SQLException()
        )
        every { accountRepository.updateAccount(match { !it.isLoggedIn }) } returns Completable.complete()

        //Act, assert
        switchLoginAccount.execute(targetAccount.username)
            .test()
            .assertFailure(SQLException::class.java)
    }
}