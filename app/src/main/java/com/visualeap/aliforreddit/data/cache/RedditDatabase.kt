package com.visualeap.aliforreddit.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.visualeap.aliforreddit.data.cache.model.CurrentToken
import com.visualeap.aliforreddit.data.cache.model.TokenEntity
import com.visualeap.aliforreddit.data.cache.model.UserTokenEntity
import com.visualeap.aliforreddit.data.cache.model.UserlessTokenEntity
import javax.inject.Inject
import javax.inject.Singleton

@Database(
    entities = [TokenEntity::class, UserTokenEntity::class, UserlessTokenEntity::class, CurrentToken::class],
    version = 1
)
abstract class RedditDatabase: RoomDatabase() {
    abstract fun tokenDao(): TokenDao
}