package com.visualeap.aliforreddit.data.cache.account

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.visualeap.aliforreddit.data.cache.RedditDatabase.Companion.NOT_SET_ROW_ID
import com.visualeap.aliforreddit.data.repository.account.AccountLocalSource
import com.visualeap.aliforreddit.domain.model.Account
import io.reactivex.Completable
import io.reactivex.Single

@Dao
abstract class AccountDao {
    @Insert
    abstract fun addAccountEntity(accountEntity: AccountEntity): Completable

    @Update
    abstract fun updateAccountEntity(account: AccountEntity): Completable

    fun getAllAccountEntities(): Single<List<AccountEntity>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}