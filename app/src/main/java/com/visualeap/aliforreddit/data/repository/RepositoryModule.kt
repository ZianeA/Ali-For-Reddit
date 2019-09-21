package com.visualeap.aliforreddit.data.repository

import com.visualeap.aliforreddit.data.repository.account.AccountDataRepository
import com.visualeap.aliforreddit.data.repository.post.PostDataRepository
import com.visualeap.aliforreddit.data.repository.redditor.RedditorDataRepository
import com.visualeap.aliforreddit.data.repository.token.TokenDataRepository
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
}