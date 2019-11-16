package com.visualeap.aliforreddit.presentation.main

import com.visualeap.aliforreddit.presentation.di.FragmentScope
import com.visualeap.aliforreddit.presentation.main.frontPage.FrontPageContainerFragment
import com.visualeap.aliforreddit.presentation.main.frontPage.FrontPageFragment
import com.visualeap.aliforreddit.presentation.main.frontPage.FrontPageModule
import com.visualeap.aliforreddit.presentation.main.postDetail.PostDetailFragment
import com.visualeap.aliforreddit.presentation.main.postDetail.PostDetailModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBindingModule {
    @FragmentScope
    @ContributesAndroidInjector(modules = [FrontPageModule::class])
    abstract fun frontPageFragment(): FrontPageFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [PostDetailModule::class])
    abstract fun postDetailFragment(): PostDetailFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun frontPageContainerFragment(): FrontPageContainerFragment
}