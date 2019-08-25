package com.visualeap.aliforreddit.data.repository.account

import com.visualeap.aliforreddit.domain.model.Account
import io.reactivex.Completable
import io.reactivex.Single

interface AccountLocalSource {
    fun updateAccount(account: Account): Completable
    fun getAllAccounts(): Single<List<Account>>
    fun addAccount(account: Account): Completable
}
