package com.visualeap.aliforreddit.data.repository

import com.visualeap.aliforreddit.domain.repository.AccountRepository
import com.visualeap.aliforreddit.domain.repository.PostRepository
import com.visualeap.aliforreddit.domain.repository.TokenRepository
import dagger.Binds
import dagger.Module
import javax.inject.Inject

@Module
interface RepositoryModule {

    @Binds
    fun providePostRepository(postDataRepository: PostDataRepository): PostRepository

    @Binds
    fun provideAccountRepository(accountDataRepository: AccountDataRepository): AccountRepository

    @Binds
    fun provideTokenRepository(tokenDataRepository: TokenDataRepository): TokenRepository
}