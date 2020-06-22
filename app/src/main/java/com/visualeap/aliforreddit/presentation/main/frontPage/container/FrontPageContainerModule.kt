package com.visualeap.aliforreddit.presentation.main.frontPage.container

import dagger.Binds
import dagger.Module

@Module
interface FrontPageContainerModule {
    @Binds
    fun provideFrontPageContainerView(fragment: FrontPageContainerFragment): FrontPageContainerView
}