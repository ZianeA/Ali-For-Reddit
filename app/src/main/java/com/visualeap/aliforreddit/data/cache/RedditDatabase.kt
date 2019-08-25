package com.visualeap.aliforreddit.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.visualeap.aliforreddit.data.cache.account.AccountDao
import com.visualeap.aliforreddit.data.cache.account.AccountEntity
import com.visualeap.aliforreddit.data.cache.redditor.RedditorDao
import com.visualeap.aliforreddit.data.cache.redditor.RedditorEntity
import com.visualeap.aliforreddit.data.cache.token.TokenDao
import com.visualeap.aliforreddit.data.cache.token.CurrentTokenEntity
import com.visualeap.aliforreddit.data.cache.token.TokenEntity
import com.visualeap.aliforreddit.data.cache.token.UserTokenEntity
import com.visualeap.aliforreddit.data.cache.token.UserlessTokenEntity

@Database(
    entities = [TokenEntity::class, UserTokenEntity::class, UserlessTokenEntity::class,
        CurrentTokenEntity::class, AccountEntity::class, RedditorEntity::class],
    version = 1
)
abstract class RedditDatabase : RoomDatabase() {
    companion object {
        const val NOT_SET_ROW_ID = 0
        const val SINGLE_RECORD_ID = 1
    }

    abstract fun tokenDao(): TokenDao
    abstract fun accountDao(): AccountDao
    abstract fun redditorDao(): RedditorDao
}