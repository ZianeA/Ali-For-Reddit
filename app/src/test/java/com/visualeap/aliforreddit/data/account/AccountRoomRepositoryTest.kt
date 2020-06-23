package com.visualeap.aliforreddit.data.account

import io.mockk.clearAllMocks
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
class AccountRoomRepositoryTest {
    private val accountDao: AccountDao = mockk()
    private val repository =
        AccountRoomRepository(accountDao)

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
    }

    /*@Test
    fun `add account`() {
        //Arrange
        every { accountDao.addAccount(any()) } returns Completable.complete()

        //Act, Assert
        val account = createAccount()
        repository.addAccount(account)
            .test()
            .assertResult()

        verify { accountDao.addAccount(account) }
    }

    @Test
    fun `return all accounts`() {
        //Arrange
        val account = createAccount()
        every { accountDao.getAllAccounts() } returns Single.just(listOf(account, account))

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
            every { accountDao.updateAccount(any()) } returns Completable.complete()

            //Act, Assert
            val account = createAccount()
            repository.updateAccount(account)
                .test()
                .assertResult()

            verify { accountDao.updateAccount(account) }
        }
    }*/
}