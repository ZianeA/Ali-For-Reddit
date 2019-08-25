package com.visualeap.aliforreddit.data.repository.account

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import util.domain.createAccount

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
class AccountDataRepositoryTest {
    private val accountLocalSource: AccountLocalSource = mockk()
    private val repository =
        AccountDataRepository(accountLocalSource)

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
    }

    @Test
    fun `add account`() {
        //Arrange
        every { accountLocalSource.addAccount(any()) } returns Completable.complete()

        //Act, Assert
        val account = createAccount()
        repository.addAccount(account)
            .test()
            .assertResult()

        verify { accountLocalSource.addAccount(account) }
    }

    @Test
    fun `return all accounts`() {
        //Arrange
        val account = createAccount()
        every { accountLocalSource.getAllAccounts() } returns Single.just(listOf(account, account))

        //Act, Assert
        repository.getAccounts()
            .test()
            .assertResult(listOf(account, account))
    }

    @Nested
    inner class UpdateAccount {
        @Test
        fun `update account`() {
            //Arrange
            every { accountLocalSource.updateAccount(any()) } returns Completable.complete()

            //Act, Assert
            val account = createAccount()
            repository.updateAccount(account)
                .test()
                .assertResult()

            verify { accountLocalSource.updateAccount(account) }
        }
    }
}