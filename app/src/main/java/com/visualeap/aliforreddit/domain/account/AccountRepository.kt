package com.visualeap.aliforreddit.domain.account

import io.reactivex.Completable
import io.reactivex.Single

interface AccountRepository {
    fun getAccounts(): Single<List<Account>>
    fun addAccount(account: Account): Completable
    fun updateAccount(account: Account): Completable
}
