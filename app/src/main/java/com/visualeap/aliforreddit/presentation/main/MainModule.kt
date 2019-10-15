package com.visualeap.aliforreddit.presentation.main

import com.visualeap.aliforreddit.presentation.di.ActivityScope
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
}