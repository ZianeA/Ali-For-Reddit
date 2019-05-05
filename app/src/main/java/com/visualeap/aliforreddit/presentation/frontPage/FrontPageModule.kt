package com.visualeap.aliforreddit.presentation.frontPage

import com.visualeap.aliforreddit.core.di.FragmentScope
import dagger.Binds
import dagger.Module

@Module
abstract class FrontPageModule {

    @FragmentScope
    @Binds
    abstract fun provideFrontPageView(frontPageFragment: FrontPageFragment): FrontPageView
}