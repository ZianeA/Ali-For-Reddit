package com.visualeap.aliforreddit.data.database

import dagger.Module
import androidx.room.Room
import android.app.Application
import androidx.core.content.contentValuesOf
import androidx.room.OnConflictStrategy
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.visualeap.aliforreddit.data.account.AccountDao
import com.visualeap.aliforreddit.data.afterkey.FeedAfterKeyDao
import com.visualeap.aliforreddit.data.redditor.RedditorDao
import com.visualeap.aliforreddit.data.post.PostDao
import com.visualeap.aliforreddit.data.token.TokenDao
import com.visualeap.aliforreddit.data.comment.CommentDao
import com.visualeap.aliforreddit.data.feed.FeedDao
import com.visualeap.aliforreddit.data.post.postfeed.PostFeedDao
import com.visualeap.aliforreddit.data.subreddit.SubredditDao
import com.visualeap.aliforreddit.data.token.CurrentTokenDao
import com.visualeap.aliforreddit.data.token.UserTokenDao
import com.visualeap.aliforreddit.data.token.UserlessTokenDao
import com.visualeap.aliforreddit.domain.feed.SortType
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    companion object {
        private const val SORT_TYPE_COLUMN_NAME = "name"
        private const val SORT_TYPE_TABLE_NAME = "SortTypeEntity"
    }

    @Singleton
    @Provides
    fun provideRedditDatabase(app: Application): RedditDatabase {
        return Room.databaseBuilder(app, RedditDatabase::class.java, "reddit")
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    seedDatabase(db)
                }
            })
            .build()
    }

    private fun seedDatabase(db: SupportSQLiteDatabase) {
        SortType.values().forEach {
            db.insert(
                SORT_TYPE_TABLE_NAME,
                OnConflictStrategy.IGNORE,
                contentValuesOf(SORT_TYPE_COLUMN_NAME to it.name)
            )
        }
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
    fun provideSubredditDao(database: RedditDatabase): SubredditDao {
        return database.subredditDao()
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

    @Singleton
    @Provides
    fun provideFeedAfterKeyDao(database: RedditDatabase): FeedAfterKeyDao {
        return database.feedAfterKeyDao()
    }
}