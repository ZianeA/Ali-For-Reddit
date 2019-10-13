package com.visualeap.aliforreddit.data.repository

import com.visualeap.aliforreddit.data.repository.account.AccountDataRepository
import com.visualeap.aliforreddit.data.repository.post.*
import com.visualeap.aliforreddit.data.repository.redditor.*
import com.visualeap.aliforreddit.data.repository.subreddit.SubredditEntity
import com.visualeap.aliforreddit.data.repository.subreddit.SubredditEntityMapper
import com.visualeap.aliforreddit.data.repository.subreddit.SubredditResponse
import com.visualeap.aliforreddit.data.repository.subreddit.SubredditResponseMapper
import com.visualeap.aliforreddit.data.repository.token.TokenDataRepository
import com.visualeap.aliforreddit.domain.model.Post
import com.visualeap.aliforreddit.domain.model.Redditor
import com.visualeap.aliforreddit.domain.model.Subreddit
import com.visualeap.aliforreddit.domain.repository.AccountRepository
import com.visualeap.aliforreddit.domain.repository.PostRepository
import com.visualeap.aliforreddit.domain.repository.RedditorRepository
import com.visualeap.aliforreddit.domain.repository.TokenRepository
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {
    @Binds
    fun providePostRepository(postDataRepository: PostDataRepository): PostRepository

    @Binds
    fun provideAccountRepository(accountDataRepository: AccountDataRepository): AccountRepository

    @Binds
    fun provideTokenRepository(tokenDataRepository: TokenDataRepository): TokenRepository

    @Binds
    fun provideRedditorRepository(RedditorDataRepository: RedditorDataRepository): RedditorRepository

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
}