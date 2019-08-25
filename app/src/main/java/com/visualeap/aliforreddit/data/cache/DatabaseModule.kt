package com.visualeap.aliforreddit.data.cache

import dagger.Module
import androidx.room.Room
import android.app.Application
import com.visualeap.aliforreddit.data.cache.account.AccountDao
import com.visualeap.aliforreddit.data.cache.account.AccountLs
import com.visualeap.aliforreddit.data.cache.token.TokenDao
import com.visualeap.aliforreddit.data.cache.token.TokenLs
import com.visualeap.aliforreddit.data.repository.account.AccountLocalSource
import com.visualeap.aliforreddit.data.repository.redditor.RedditorLocalSource
import com.visualeap.aliforreddit.data.repository.token.TokenLocalSource
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideRedditDatabase(app: Application): RedditDatabase {
        return Room.databaseBuilder(app, RedditDatabase::class.java, "reddit")
            .build()
    }

    @Singleton
    @Provides
    fun provideTokenDao(database: RedditDatabase): TokenDao {
        return database.tokenDao()
    }

    @Singleton
    @Provides
    fun provideTokenLocalSource(tokenLs: TokenLs): TokenLocalSource = tokenLs

    @Singleton
    @Provides
    fun provideAccountDao(database: RedditDatabase): AccountDao {
        return database.accountDao()
    }

    @Singleton
    @Provides
    fun provideAccountLocalSource(accountLs: AccountLs): AccountLocalSource = accountLs

    @Singleton
    @Provides
    fun provideRedditorDao(database: RedditDatabase): RedditorLocalSource {
        return database.redditorDao()
    }
}