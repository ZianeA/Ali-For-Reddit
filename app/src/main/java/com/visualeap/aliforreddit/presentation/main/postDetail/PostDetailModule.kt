package com.visualeap.aliforreddit.presentation.main.postDetail

import dagger.Binds
import dagger.Module

@Module
interface PostDetailModule {
    @Binds
    fun providePostDetailView(fragment: PostDetailFragment): PostDetailView
}