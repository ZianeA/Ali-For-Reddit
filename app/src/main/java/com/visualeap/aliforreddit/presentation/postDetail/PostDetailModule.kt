package com.visualeap.aliforreddit.presentation.postDetail

import dagger.Module
import dagger.Provides

@Module
class PostDetailModule {
    @Provides
    fun providePostDetailView(fragment: PostDetailFragment): PostDetailLauncher = fragment

    @Provides
    @PostId
    fun providePostId(fragment: PostDetailFragment): String = fragment.postId

    @Provides
    @Subreddit
    fun provideSubredditId(fragment: PostDetailFragment): String = fragment.subreddit
}