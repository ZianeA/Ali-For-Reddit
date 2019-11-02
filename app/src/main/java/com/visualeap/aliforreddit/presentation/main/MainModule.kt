package com.visualeap.aliforreddit.presentation.main

import com.visualeap.aliforreddit.domain.model.Post
import com.visualeap.aliforreddit.domain.model.Subreddit
import com.visualeap.aliforreddit.domain.util.Mapper
import com.visualeap.aliforreddit.presentation.di.ActivityScope
import com.visualeap.aliforreddit.presentation.mapper.PostViewMapper
import com.visualeap.aliforreddit.presentation.mapper.SubredditViewMapper
import com.visualeap.aliforreddit.presentation.model.PostView
import com.visualeap.aliforreddit.presentation.model.SubredditView
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
class MainModule {
    @Provides
    fun provideMainView(mainActivity: MainActivity): MainView = mainActivity

    @ActivityScope
    @Provides
    fun provideFragNavController(mainActivity: MainActivity) = mainActivity.fragNavController

    @Provides
    fun providePostViewMapper(mapper: PostViewMapper): Mapper<PostView, Post> = mapper

    @Provides
    fun provideSubredditViewMapper(mapper: SubredditViewMapper): Mapper<SubredditView, Subreddit> =
        mapper
}