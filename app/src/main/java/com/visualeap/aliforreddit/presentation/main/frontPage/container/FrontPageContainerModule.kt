package com.visualeap.aliforreddit.presentation.main.frontPage.container

import com.visualeap.aliforreddit.presentation.main.frontPage.FrontPageView
import dagger.Binds
import dagger.Module

@Module
interface FrontPageContainerModule {
    @Binds
    fun provideFrontPageContainerView(fragment: FrontPageContainerFragment): FrontPageContainerView
}