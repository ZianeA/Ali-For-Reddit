package com.visualeap.aliforreddit.data.repository.account

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import io.reactivex.Completable
import io.reactivex.Single

@Dao
abstract class AccountDao {
    @Insert
    abstract fun add(accountEntity: AccountEntity): Completable

    @Update
    abstract fun update(account: AccountEntity): Completable

    fun getAll(): Single<List<AccountEntity>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}