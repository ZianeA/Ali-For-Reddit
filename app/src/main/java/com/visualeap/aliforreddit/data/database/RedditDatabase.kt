package com.visualeap.aliforreddit.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.visualeap.aliforreddit.data.account.AccountDao
import com.visualeap.aliforreddit.data.account.AccountEntity
import com.visualeap.aliforreddit.data.afterkey.FeedAfterKeyDao
import com.visualeap.aliforreddit.data.afterkey.FeedAfterKeyEntity
import com.visualeap.aliforreddit.data.post.PostDao
import com.visualeap.aliforreddit.data.post.PostEntity
import com.visualeap.aliforreddit.data.redditor.RedditorDao
import com.visualeap.aliforreddit.data.redditor.RedditorEntity
import com.visualeap.aliforreddit.data.comment.CommentDao
import com.visualeap.aliforreddit.data.comment.CommentEntity
import com.visualeap.aliforreddit.data.feed.FeedDao
import com.visualeap.aliforreddit.data.feed.FeedEntity
import com.visualeap.aliforreddit.data.post.postfeed.PostFeedDao
import com.visualeap.aliforreddit.data.post.postfeed.PostFeedEntity
import com.visualeap.aliforreddit.data.sort.SortTypeEntity
import com.visualeap.aliforreddit.data.subreddit.SubredditDao
import com.visualeap.aliforreddit.data.subreddit.SubredditEntity
import com.visualeap.aliforreddit.data.token.*

@Database(
    entities = [TokenEntity::class, UserTokenEntity::class, UserlessTokenEntity::class,
        CurrentTokenEntity::class, AccountEntity::class, RedditorEntity::class, PostEntity::class,
        SubredditEntity::class, CommentEntity::class, FeedEntity::class, PostFeedEntity::class,
        SortTypeEntity::class, FeedAfterKeyEntity::class],
    version = 1
)
@TypeConverters(
    CurrentTokenEntity.TokenTypeConverter::class,
    SortTypeEntity.SortTypeConverter::class
)
abstract class RedditDatabase : RoomDatabase() {
    abstract fun tokenDao(): TokenDao
    abstract fun userTokenDao(): UserTokenDao
    abstract fun userlessTokenDao(): UserlessTokenDao
    abstract fun currentTokenDao(): CurrentTokenDao
    abstract fun accountDao(): AccountDao
    abstract fun redditorDao(): RedditorDao
    abstract fun subredditDao(): SubredditDao
    abstract fun postDao(): PostDao
    abstract fun commentDao(): CommentDao
    abstract fun feedDao(): FeedDao
    abstract fun postFeedDao(): PostFeedDao
    abstract fun feedAfterKeyDao(): FeedAfterKeyDao
}