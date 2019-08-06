package com.visualeap.aliforreddit.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.visualeap.aliforreddit.data.cache.model.CurrentTokenEntity
import com.visualeap.aliforreddit.data.cache.model.TokenEntity
import com.visualeap.aliforreddit.data.cache.model.UserTokenEntity
import com.visualeap.aliforreddit.data.cache.model.UserlessTokenEntity

@Database(
    entities = [TokenEntity::class, UserTokenEntity::class, UserlessTokenEntity::class, CurrentTokenEntity::class],
    version = 1
)
abstract class RedditDatabase: RoomDatabase() {
    abstract fun tokenDao(): TokenDao
}