package com.visualeap.aliforreddit.presentation.postDetail

import dagger.Binds
import dagger.Module

@Module
interface PostDetailModule {
    @Binds
    fun providePostDetailView(fragment: PostDetailFragment): PostDetailView
}