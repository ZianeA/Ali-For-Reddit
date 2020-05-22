package com.visualeap.aliforreddit.data.cache

import dagger.Module
import androidx.room.Room
import android.app.Application
import androidx.core.content.contentValuesOf
import androidx.room.OnConflictStrategy
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.visualeap.aliforreddit.data.repository.account.AccountDao
import com.visualeap.aliforreddit.data.repository.redditor.RedditorDao
import com.visualeap.aliforreddit.data.repository.post.PostDao
import com.visualeap.aliforreddit.data.repository.token.TokenDao
import com.visualeap.aliforreddit.data.repository.comment.CommentDao
import com.visualeap.aliforreddit.domain.model.Feed.DefaultFeed
import com.visualeap.aliforreddit.data.repository.feed.FeedDao
import com.visualeap.aliforreddit.data.repository.post.postfeed.PostFeedDao
import com.visualeap.aliforreddit.data.repository.token.CurrentTokenDao
import com.visualeap.aliforreddit.data.repository.token.UserTokenDao
import com.visualeap.aliforreddit.data.repository.token.UserlessTokenDao
import com.visualeap.aliforreddit.domain.model.Feed.FeedType
import com.visualeap.aliforreddit.domain.model.Feed.SortBy
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideRedditDatabase(app: Application): RedditDatabase {
        return Room.databaseBuilder(app, RedditDatabase::class.java, "reddit")
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    //TODO move this code to an adequate place
                    val FEED_COLUMN_NAME = "name"
                    val FEED_COLUMN_SORT_BY = "sortby"
                    val FEED_COLUMN_AFTER_KEY = "afterKey"
                    val FEED_COLUMN_TYPE = "type"
                    val FEED_ENTITY_TABLE_NAME = "FeedEntity"

                    DefaultFeed.values().forEach {
                        db.insert(
                            FEED_ENTITY_TABLE_NAME,
                            OnConflictStrategy.IGNORE,
                            contentValuesOf(
                                FEED_COLUMN_NAME to it.name,
                                FEED_COLUMN_SORT_BY to SortBy.Best.name,
                                FEED_COLUMN_AFTER_KEY to null,
                                FEED_COLUMN_TYPE to FeedType.Default.name
                            )
                        )
                    }
                }
            })
            .build()
    }

    @Singleton
    @Provides
    fun provideTokenDao(database: RedditDatabase): TokenDao {
        return database.tokenDao()
    }

    @Singleton
    @Provides
    fun provideUserTokenDao(database: RedditDatabase): UserTokenDao {
        return database.userTokenDao()
    }

    @Singleton
    @Provides
    fun provideUserlessTokenDao(database: RedditDatabase): UserlessTokenDao {
        return database.userlessTokenDao()
    }

    @Singleton
    @Provides
    fun provideCurrentTokenDao(database: RedditDatabase): CurrentTokenDao {
        return database.currentTokenDao()
    }

    @Singleton
    @Provides
    fun provideAccountDao(database: RedditDatabase): AccountDao {
        return database.accountDao()
    }

    @Singleton
    @Provides
    fun provideRedditorDao(database: RedditDatabase): RedditorDao {
        return database.redditorDao()
    }

    @Singleton
    @Provides
    fun providePostDao(database: RedditDatabase): PostDao {
        return database.postDao()
    }

    @Singleton
    @Provides
    fun provideCommentDao(database: RedditDatabase): CommentDao {
        return database.commentDao()
    }

    @Singleton
    @Provides
    fun provideFeedDao(database: RedditDatabase): FeedDao {
        return database.feedDao()
    }

    @Singleton
    @Provides
    fun providePostFeedDao(database: RedditDatabase): PostFeedDao {
        return database.postFeedDao()
    }
}