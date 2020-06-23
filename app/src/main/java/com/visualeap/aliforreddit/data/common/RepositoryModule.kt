package com.visualeap.aliforreddit.data.common

import com.visualeap.aliforreddit.data.account.AccountRoomRepository
import com.visualeap.aliforreddit.data.afterkey.AfterKeyRoomRepository
import com.visualeap.aliforreddit.data.comment.*
import com.visualeap.aliforreddit.data.feed.FeedRoomRepository
import com.visualeap.aliforreddit.data.post.*
import com.visualeap.aliforreddit.data.redditor.*
import com.visualeap.aliforreddit.data.subreddit.*
import com.visualeap.aliforreddit.data.token.*
import com.visualeap.aliforreddit.domain.account.AccountRepository
import com.visualeap.aliforreddit.domain.authentication.TokenRepository
import com.visualeap.aliforreddit.domain.comment.CommentRepository
import com.visualeap.aliforreddit.domain.comment.Comment
import com.visualeap.aliforreddit.domain.post.Post
import com.visualeap.aliforreddit.domain.redditor.Redditor
import com.visualeap.aliforreddit.domain.subreddit.Subreddit
import com.visualeap.aliforreddit.domain.post.AfterKeyRepository
import com.visualeap.aliforreddit.domain.feed.FeedRepository
import com.visualeap.aliforreddit.domain.post.PostRepository
import com.visualeap.aliforreddit.domain.redditor.RedditorRepository
import com.visualeap.aliforreddit.domain.subreddit.SubredditRepository
import com.visualeap.aliforreddit.domain.util.Mapper
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {
    @Binds
    fun providePostRepository(postRepo: PostRoomRepository): PostRepository

    @Binds
    fun provideSubredditRepository(subredditRepo: SubredditRoomRepository): SubredditRepository

    @Binds
    fun provideAccountRepository(accountRepo: AccountRoomRepository): AccountRepository

    @Binds
    fun provideTokenRepository(tokenRepo: TokenRoomRepository): TokenRepository

    @Binds
    fun provideRedditorRepository(redditorRepo: RedditorRoomRepository): RedditorRepository

    @Binds
    fun provideCommentRepository(commentRepo: CommentRoomRepository): CommentRepository

    @Binds
    fun provideFeedRepository(feedRepo: FeedRoomRepository): FeedRepository

    @Binds
    fun provideAfterKeyRepository(afterKeyRepo: AfterKeyRoomRepository): AfterKeyRepository

    @Binds
    fun providePostWithSubredditResponseMapper(mapper: PostWithSubredditResponseMapper): Mapper<PostWithSubredditResponse, List<Post>>

    @Binds
    fun provideRedditorEntityMapper(mapper: RedditorEntityMapper): Mapper<RedditorEntity, Redditor>

    @Binds
    fun provideRedditorResponseMapper(mapper: RedditorResponseMapper): Mapper<RedditorResponse, Redditor>

    @Binds
    fun provideSubredditEntityMapper(mapper: SubredditEntityMapper): Mapper<SubredditEntity, Subreddit>

    @Binds
    fun provideSubredditResponseMapper(mapper: SubredditResponseMapper): Mapper<SubredditResponse, List<Subreddit>>

    @Binds
    fun provideCommentResponseMapper(mapper: CommentResponseMapper): Mapper<CommentResponse, List<Comment>>

    @Binds
    fun provideCommentEntityMapper(mapper: CommentEntityMapper): Mapper<List<CommentEntity>, List<Comment>>
}