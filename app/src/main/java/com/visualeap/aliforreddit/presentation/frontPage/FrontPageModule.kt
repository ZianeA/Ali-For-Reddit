package com.visualeap.aliforreddit.presentation.frontPage

import dagger.Module
import dagger.Provides

@Module
class FrontPageModule {
    @Provides
    fun provideFrontPageView(frontPageFragment: FrontPageFragment): FrontPageLauncher {
        return frontPageFragment
    }

    @Provides
    @Feed
    fun provideFeed(frontPageFragment: FrontPageFragment): String = frontPageFragment.feed
}