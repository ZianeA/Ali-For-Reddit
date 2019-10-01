package com.visualeap.aliforreddit.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.visualeap.aliforreddit.data.repository.account.AccountDao
import com.visualeap.aliforreddit.data.repository.account.AccountEntity
import com.visualeap.aliforreddit.data.repository.post.PostDao
import com.visualeap.aliforreddit.data.repository.post.PostEntity
import com.visualeap.aliforreddit.data.repository.redditor.RedditorDao
import com.visualeap.aliforreddit.data.repository.redditor.RedditorEntity
import com.visualeap.aliforreddit.data.cache.token.TokenDao
import com.visualeap.aliforreddit.data.cache.token.CurrentTokenEntity
import com.visualeap.aliforreddit.data.cache.token.TokenEntity
import com.visualeap.aliforreddit.data.cache.token.UserTokenEntity
import com.visualeap.aliforreddit.data.cache.token.UserlessTokenEntity
import com.visualeap.aliforreddit.data.repository.subreddit.SubredditEntity

@Database(
    entities = [TokenEntity::class, UserTokenEntity::class, UserlessTokenEntity::class,
        CurrentTokenEntity::class, AccountEntity::class, RedditorEntity::class, PostEntity::class, SubredditEntity::class],
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
    abstract fun postDao(): PostDao
}