package com.visualeap.aliforreddit.data.cache

import dagger.Module
import androidx.room.Room
import android.app.Application
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
}