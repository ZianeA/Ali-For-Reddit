package com.visualeap.aliforreddit.presentation.main

import dagger.Binds
import dagger.Module

@Module
interface MainModule {
    @Binds
    fun provideMainView(mainActivity: MainActivity): MainView
}