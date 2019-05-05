package com.visualeap.aliforreddit.data.repository

import com.visualeap.aliforreddit.domain.repository.PostRepository
import dagger.Binds
import dagger.Module
import javax.inject.Inject

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun providePostRepository(postDataRepository: PostDataRepository): PostRepository
}