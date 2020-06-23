package com.visualeap.aliforreddit.presentation.common.main

import com.visualeap.aliforreddit.domain.comment.Comment
import com.visualeap.aliforreddit.domain.util.Mapper
import com.visualeap.aliforreddit.presentation.common.di.ActivityScope
import com.visualeap.aliforreddit.presentation.common.view.drawer.DrawerController
import com.visualeap.aliforreddit.presentation.common.formatter.CommentViewMapper
import com.visualeap.aliforreddit.presentation.common.model.CommentView
import dagger.Module
import dagger.Provides

@Module
class MainModule {
    @Provides
    fun provideMainView(mainActivity: MainActivity): MainView = mainActivity

    @ActivityScope
    @Provides
    fun provideFragNavController(mainActivity: MainActivity) = mainActivity.fragNavController

    @ActivityScope
    @Provides
    fun provideDrawerController(mainActivity: MainActivity): DrawerController = mainActivity

    @Provides
    fun provideCommentViewMapper(mapper: CommentViewMapper): Mapper<List<CommentView>, List<Comment>> =
        mapper
}