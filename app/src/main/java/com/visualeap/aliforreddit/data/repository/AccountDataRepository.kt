package com.visualeap.aliforreddit.data.repository

import com.visualeap.aliforreddit.domain.model.Account
import com.visualeap.aliforreddit.domain.repository.AccountRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountDataRepository @Inject constructor(): AccountRepository {
    override fun updateAccount(account: Account): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAccounts(): Single<List<Account>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveAccount(account: Account): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}