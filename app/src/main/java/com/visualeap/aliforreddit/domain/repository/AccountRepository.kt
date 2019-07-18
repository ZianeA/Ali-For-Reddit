package com.visualeap.aliforreddit.domain.repository

import com.visualeap.aliforreddit.domain.model.Account
import io.reactivex.Completable
import io.reactivex.Single

interface AccountRepository {
    fun getAccounts(): Single<List<Account>>
    fun saveAccount(account: Account): Completable
    fun updateAccount(account: Account): Completable
}
