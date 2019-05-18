package com.visualeap.aliforreddit.presentation.main.frontPage

import com.visualeap.aliforreddit.presentation.di.FragmentScope
import dagger.Binds
import dagger.Module

@Module
abstract class FrontPageModule {

    @FragmentScope
    @Binds
    abstract fun provideFrontPageView(frontPageFragment: FrontPageFragment): FrontPageView
}