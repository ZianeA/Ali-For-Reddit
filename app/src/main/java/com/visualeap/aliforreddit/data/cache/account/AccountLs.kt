package com.visualeap.aliforreddit.data.cache.account

import com.visualeap.aliforreddit.data.cache.RedditDatabase.Companion.NOT_SET_ROW_ID
import com.visualeap.aliforreddit.data.repository.account.AccountLocalSource
import com.visualeap.aliforreddit.data.repository.redditor.RedditorLocalSource
import com.visualeap.aliforreddit.data.repository.token.TokenLocalSource
import com.visualeap.aliforreddit.domain.model.Account
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountLs @Inject constructor(
    private val accountDao: AccountDao,
    private val redditorLs: RedditorLocalSource,
    private val tokenLs: TokenLocalSource
) : AccountLocalSource {
    override fun updateAccount(account: Account) = accountDao.updateAccountEntity(account.map())

    override fun getAllAccounts(): Single<List<Account>> {
        return accountDao.getAllAccountEntities()
            .flatMap { accountEntityList ->
                Observable.fromIterable(accountEntityList)
                    .flatMapSingle { accountEntity ->
                        redditorLs.getRedditor(accountEntity.redditorUsername)
                            .flatMap { redditor ->
                                tokenLs.getUserToken(accountEntity.tokenId)
                                    .map { token -> Account(accountEntity.id, redditor, token) }
                            }
                    }
                    .toList()
            }
    }

    override fun addAccount(account: Account) =
        accountDao.addAccountEntity(account.map(NOT_SET_ROW_ID))

    private fun Account.map(id: Int = this.id) = AccountEntity(id, redditor.username, token.id)
}