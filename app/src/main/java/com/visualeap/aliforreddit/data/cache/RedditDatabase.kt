package com.visualeap.aliforreddit.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.visualeap.aliforreddit.data.cache.token.TokenDao
import com.visualeap.aliforreddit.data.cache.token.CurrentTokenEntity
import com.visualeap.aliforreddit.data.cache.token.TokenEntity
import com.visualeap.aliforreddit.data.cache.token.UserTokenEntity
import com.visualeap.aliforreddit.data.cache.token.UserlessTokenEntity

@Database(
    entities = [TokenEntity::class, UserTokenEntity::class, UserlessTokenEntity::class, CurrentTokenEntity::class],
    version = 1
)
abstract class RedditDatabase : RoomDatabase() {
    companion object {
        const val NOT_SET_ROW_ID = 0
    }

    abstract fun tokenDao(): TokenDao
}