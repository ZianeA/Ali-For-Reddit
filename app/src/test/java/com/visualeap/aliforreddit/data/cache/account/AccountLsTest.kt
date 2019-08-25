package com.visualeap.aliforreddit.data.cache.account

import com.visualeap.aliforreddit.data.repository.redditor.RedditorLocalSource
import com.visualeap.aliforreddit.data.repository.token.TokenLocalSource
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import util.domain.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
internal class AccountLsTest {
    private val accountDao: AccountDao = mockk()
    private val redditorLs: RedditorLocalSource = mockk()
    private val tokenls: TokenLocalSource = mockk()
    private val accountls = AccountLs(accountDao, redditorLs, tokenls)

    @BeforeEach
    internal fun setUp() {
        clearAllMocks()
    }

    @Test
    fun `add account`() {
        //Arrange
        every { accountDao.addAccountEntity(any()) } returns Completable.complete()

        //Act, Assert
        val account = createAccount()
        accountls.addAccount(account)
            .test()
            .assertResult()

        val expectedAccountEntity =
            AccountEntity(NOT_SET_ROW_ID, account.redditor.username, account.token.id)
        verify { accountDao.addAccountEntity(expectedAccountEntity) }
    }

    @Test
    fun `update account`() {
        //Arrange
        every { accountDao.updateAccountEntity(any()) } returns Completable.complete()

        //Act, Assert
        val account = createAccount(id = randomInteger)
        accountls.updateAccount(account)
            .test()
            .assertResult()
        val expectedAccountEntity =
            AccountEntity(account.id, account.redditor.username, account.token.id)
        verify { accountDao.updateAccountEntity(expectedAccountEntity) }
    }

    @Test
    fun `return all accounts`() {
        //Arrange
        val accountEntity =
            AccountEntity(randomInteger, "RandomRedditorUsername", randomInteger)
        val redditor = createRedditor(username = accountEntity.redditorUsername)
        val token = createUserToken(id = accountEntity.tokenId)

        every { accountDao.getAllAccountEntities() } returns Single.just(
            listOf(accountEntity, accountEntity)
        )
        every { redditorLs.getRedditor(accountEntity.redditorUsername) } returns Single.just(
            redditor
        )
        every { tokenls.getUserToken(accountEntity.tokenId) } returns Single.just(token)

        //Act, Assert
        val expectedAccount = createAccount(accountEntity.id, redditor, token)
        accountls.getAllAccounts()
            .test()
            .assertResult(listOf(expectedAccount, expectedAccount))
    }
}