package com.visualeap.aliforreddit.data.repository

import com.visualeap.aliforreddit.data.repository.account.AccountDataRepository
import com.visualeap.aliforreddit.data.repository.comment.*
import com.visualeap.aliforreddit.data.repository.post.*
import com.visualeap.aliforreddit.data.repository.redditor.*
import com.visualeap.aliforreddit.data.repository.subreddit.SubredditEntity
import com.visualeap.aliforreddit.data.repository.subreddit.SubredditEntityMapper
import com.visualeap.aliforreddit.data.repository.subreddit.SubredditResponse
import com.visualeap.aliforreddit.data.repository.subreddit.SubredditResponseMapper
import com.visualeap.aliforreddit.data.repository.token.*
import com.visualeap.aliforreddit.domain.model.Comment
import com.visualeap.aliforreddit.domain.model.Post
import com.visualeap.aliforreddit.domain.model.Redditor
import com.visualeap.aliforreddit.domain.model.Subreddit
import com.visualeap.aliforreddit.domain.model.token.UserToken
import com.visualeap.aliforreddit.domain.model.token.UserlessToken
import com.visualeap.aliforreddit.domain.repository.*
import com.visualeap.aliforreddit.domain.util.Mapper
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {
    @Binds
    fun providePostRepository(postRepo: PostDataRepository): PostRepository

    @Binds
    fun provideAccountRepository(accountRepo: AccountDataRepository): AccountRepository

    @Binds
    fun provideTokenRepository(tokenRepo: TokenDataRepository): TokenRepository

    @Binds
    fun provideRedditorRepository(redditorRepo: RedditorDataRepository): RedditorRepository

    @Binds
    fun provideCommentRepository(commentRepo: CommentDataRepository): CommentRepository

    @Binds
    fun providePostWithSubredditEntityMapper(mapper: PostWithSubredditEntityMapper): Mapper<PostWithSubredditEntity, Post>

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

    @Binds
    fun provideUserTokenMapper(mapper: TokenWithUserTokenEntityMapper): Mapper<TokenWithUserTokenEntity, UserToken>

    @Binds
    fun provideUserlessTokenMapper(mapper: TokenWithUserlessTokenEntityMapper): Mapper<TokenWithUserlessTokenEntity, UserlessToken>

    @Binds
    fun provideNextPageKeyStore(store: NextPageKeyStore): KeyValueStore<String?>
}