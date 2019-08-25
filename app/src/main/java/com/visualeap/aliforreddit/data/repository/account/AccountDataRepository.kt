package com.visualeap.aliforreddit.data.repository.account

import com.visualeap.aliforreddit.data.cache.account.AccountEntity
import com.visualeap.aliforreddit.data.repository.redditor.RedditorLocalSource
import com.visualeap.aliforreddit.data.repository.token.TokenLocalSource
import com.visualeap.aliforreddit.domain.model.Account
import com.visualeap.aliforreddit.domain.repository.AccountRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountDataRepository @Inject constructor(private val accountLocalSource: AccountLocalSource) :
    AccountRepository {
    override fun updateAccount(account: Account) = accountLocalSource.updateAccount(account)
    override fun getAccounts(): Single<List<Account>> = accountLocalSource.getAllAccounts()
    override fun addAccount(account: Account) = accountLocalSource.addAccount(account)
}