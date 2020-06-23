package com.visualeap.aliforreddit.data.account

import com.visualeap.aliforreddit.domain.account.Account
import com.visualeap.aliforreddit.domain.account.AccountRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRoomRepository @Inject constructor(private val accountDao: AccountDao) :
    AccountRepository {
    override fun getAccounts(): Single<List<Account>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addAccount(account: Account): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateAccount(account: Account): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}